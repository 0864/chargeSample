package code.sample.premcharge.charge.repository;

import code.sample.premcharge.charge.domain.ItemStatus;
import code.sample.premcharge.charge.domain.JobItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface JobItemRepository extends JpaRepository<JobItem, Long> {

    Page<JobItem> findByJobIdAndStatus(Long jobId, ItemStatus status, Pageable pageable);

    @Modifying
    @Query("update JobItem i set i.status=:status, i.warnCnt=:warnCnt, i.errCnt=:errCnt, i.finishedAt=current_timestamp where i.id=:id")
    void updateFinal(@Param("id") Long id,
                     @Param("status") ItemStatus status,
                     @Param("warnCnt") int warnCnt,
                     @Param("errCnt") int errCnt);

    @Modifying
    @Query("update JobItem i set i.status='FAILED', i.errCnt=i.errCnt+1, i.errorCode=:err where i.id=:id")
    void markFailed(@Param("id") Long id, @Param("err") String errorCode);

    @Query("select count(i) from JobItem i where i.jobId=:jobId and i.status='SUCCESS'")
    long countSuccess(@Param("jobId") Long jobId);

    @Query("select count(i) from JobItem i where i.jobId=:jobId and i.status='FAILED'")
    long countFailed(@Param("jobId") Long jobId);

    @Query("select coalesce(sum(i.warnCnt),0) from JobItem i where i.jobId=:jobId")
    long sumWarns(@Param("jobId") Long jobId);
}