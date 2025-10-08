package code.sample.premcharge.charge.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "job_item",
        indexes = {
                @Index(name="idx_jobitem_jobid", columnList = "jobId"),
                @Index(name="idx_jobitem_policyid", columnList = "policyId")
        })
public class JobItem {
    @Id
    private Long id;

    private Long jobId;
    private Long policyId;
    @Enumerated(EnumType.STRING)
    private ItemStatus status = ItemStatus.PENDING;

    private Integer warnCnt = 0;
    private Integer errCnt = 0;
    private String errorCode;     // 当 FAILED 时填
    private OffsetDateTime finishedAt;

    // 业务必要的原始字段（例如充值金额、凭证号等）按需补充
    private Long amount;          // 示例

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Integer getWarnCnt() {
        return warnCnt;
    }

    public void setWarnCnt(Integer warnCnt) {
        this.warnCnt = warnCnt;
    }

    public Integer getErrCnt() {
        return errCnt;
    }

    public void setErrCnt(Integer errCnt) {
        this.errCnt = errCnt;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public OffsetDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(OffsetDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}