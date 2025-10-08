package code.sample.premcharge.charge.service;

import code.sample.premcharge.charge.domain.JobItem;

import java.util.List;

public interface AccountingService {
    void postEntries(JobItem item, List<String> warnings);
}
