package code.sample.premcharge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean("rechargeExecutor")
    public Executor rechargeExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(32);
        ex.setMaxPoolSize(64);
        ex.setQueueCapacity(2000);
        ex.setKeepAliveSeconds(60);
        ex.setThreadNamePrefix("recharge-");
        ex.setWaitForTasksToCompleteOnShutdown(true);
        ex.initialize();
        return ex;
    }
}
