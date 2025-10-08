package code.sample.premcharge.charge.service;

import code.sample.premcharge.charge.domain.JobItem;

import java.util.List;

public interface FinanceService {
    void applyRecharge(JobItem item, List<String> warnings);
}
