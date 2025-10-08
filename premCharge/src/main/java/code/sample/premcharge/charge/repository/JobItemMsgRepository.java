package code.sample.premcharge.charge.repository;

import code.sample.premcharge.charge.domain.JobItemMsg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobItemMsgRepository extends JpaRepository<JobItemMsg, Long> {
}