package code.sample.premcharge.charge.service;

import code.sample.premcharge.charge.domain.JobItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountingServiceImpl implements AccountingService {
    @Override
    public void postEntries(JobItem item, List<String> warnings) {

    }
}
