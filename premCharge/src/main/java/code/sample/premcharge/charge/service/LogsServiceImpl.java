package code.sample.premcharge.charge.service;

import code.sample.premcharge.charge.domain.JobItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogsServiceImpl implements LogsService {
    @Override
    public void backupAll(JobItem item, List<String> warnings) {

    }
}
