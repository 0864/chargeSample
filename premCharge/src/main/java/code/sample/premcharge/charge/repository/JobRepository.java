package code.sample.premcharge.charge.repository;

import code.sample.premcharge.charge.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
