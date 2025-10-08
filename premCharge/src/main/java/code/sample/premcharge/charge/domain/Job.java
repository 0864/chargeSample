package code.sample.premcharge.charge.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "job")
public class Job {
    @Id
    private Long id;

    private String status; // RUNNING / FINISHED / PARTIAL_FAILED
    private Long totalCnt;
    private Long succCnt;
    private Long failCnt;
    private Long warnCntTotal;
    private OffsetDateTime startedAt;
    private OffsetDateTime finishedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(Long totalCnt) {
        this.totalCnt = totalCnt;
    }

    public Long getSuccCnt() {
        return succCnt;
    }

    public void setSuccCnt(Long succCnt) {
        this.succCnt = succCnt;
    }

    public Long getFailCnt() {
        return failCnt;
    }

    public void setFailCnt(Long failCnt) {
        this.failCnt = failCnt;
    }

    public Long getWarnCntTotal() {
        return warnCntTotal;
    }

    public void setWarnCntTotal(Long warnCntTotal) {
        this.warnCntTotal = warnCntTotal;
    }

    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public OffsetDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(OffsetDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }
}