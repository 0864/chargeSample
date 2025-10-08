package code.sample.premcharge.charge.service;

import code.sample.premcharge.charge.ShardedExecutor;
import code.sample.premcharge.charge.domain.ItemStatus;
import code.sample.premcharge.charge.domain.JobItem;
import code.sample.premcharge.charge.domain.JobItemMsg;
import code.sample.premcharge.charge.domain.MessageType;
import code.sample.premcharge.charge.repository.JobItemMsgRepository;
import code.sample.premcharge.charge.repository.JobItemRepository;
import code.sample.premcharge.charge.repository.JobRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class RechargeJobService {

    private final JobRepository jobRepo;
    private final JobItemRepository itemRepo;
    private final JobItemMsgRepository msgRepo;
    private final LogsService logsService;
    private final FinanceService financeService;
    private final AccountingService accountingService;
    private final EntityManager em;
    private final Executor rechargeExecutor;

    @Value("${recharge.shards:64}")
    private int shards;

    public RechargeJobService(JobRepository jobRepo,
                              JobItemRepository itemRepo,
                              JobItemMsgRepository msgRepo,
                              LogsService logsService,
                              FinanceService financeService,
                              AccountingService accountingService,
                              EntityManager em,
                              Executor rechargeExecutor) {
        this.jobRepo = jobRepo;
        this.itemRepo = itemRepo;
        this.msgRepo = msgRepo;
        this.logsService = logsService;
        this.financeService = financeService;
        this.accountingService = accountingService;
        this.em = em;
        this.rechargeExecutor = rechargeExecutor;
    }

    @Async("rechargeExecutor")
    public void startJob(Long jobId) {
        jobRepo.findById(jobId).ifPresent(job -> {
            job.setStatus("RUNNING");
            job.setStartedAt(OffsetDateTime.now());
            jobRepo.save(job);
        });

        PageRequest pr = PageRequest.of(0, 2000);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        try (ShardedExecutor sharded = new ShardedExecutor(shards, "recharge")) {
            while (true) {
                Page<JobItem> page = itemRepo.findByJobIdAndStatus(jobId, ItemStatus.PENDING, pr);
                if (page.isEmpty()) break;

                for (JobItem item : page.getContent()) {
                    // 使用 CompletableFuture 提交到 ShardedExecutor 中
                    CompletableFuture<Void> future = sharded.submitAsync(item.getId(), () -> {
                        try {
                            processOne(item.getId());
                        } catch (Exception e) {
                            itemRepo.markFailed(item.getId(), "UNCAUGHT");
                            JobItemMsg m = new JobItemMsg();
                            m.setItemId(item.getId());
                            m.setType(MessageType.ERROR);
                            m.setCode("UNCAUGHT");
                            m.setMessage(e.getMessage());
                            msgRepo.save(m);
                        }
                    });
                    futures.add(future);
                }

                if (!page.hasNext()) break;
                pr = (PageRequest) page.nextPageable();
            }

            // 等待所有任务完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }

        jobRepo.findById(jobId).ifPresent(job -> {
            long succ = itemRepo.countSuccess(jobId);
            long fail = itemRepo.countFailed(jobId);
            long warn = itemRepo.sumWarns(jobId);
            job.setSuccCnt(succ);
            job.setFailCnt(fail);
            job.setWarnCntTotal(warn);
            job.setFinishedAt(OffsetDateTime.now());
            job.setStatus(fail > 0 ? "PARTIAL_FAILED" : "FINISHED");
            jobRepo.save(job);
        });
    }

    @Transactional
    public void processOne(Long itemId) {
        JobItem item = itemRepo.findById(itemId).orElseThrow();
        List<String> warnings = new ArrayList<>();

        try {
            logsService.backupAll(item, warnings);
            financeService.applyRecharge(item, warnings);
            accountingService.postEntries(item, warnings);

            itemRepo.updateFinal(item.getId(), ItemStatus.SUCCESS, warnings.size(), 0);

            if (!warnings.isEmpty()) {
                List<JobItemMsg> batch = new ArrayList<>(warnings.size());
                for (String w : warnings) {
                    JobItemMsg m = new JobItemMsg();
                    m.setItemId(item.getId());
                    m.setType(MessageType.WARNING);
                    m.setCode("WARN");
                    m.setMessage(w);
                    batch.add(m);
                }
                msgRepo.saveAll(batch);
            }

        } catch (RuntimeException ex) {
            itemRepo.markFailed(item.getId(), "BIZ_ERR");
            JobItemMsg m = new JobItemMsg();
            m.setItemId(item.getId());
            m.setType(MessageType.ERROR);
            m.setCode("BIZ_ERR");
            m.setMessage(ex.getMessage());
            msgRepo.save(m);
            throw ex;
        }
    }
}
