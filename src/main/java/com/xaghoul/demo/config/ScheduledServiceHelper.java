package com.xaghoul.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ScheduledServiceHelper {
    @Bean
    public TaskScheduler scheduledExecutorService() {
        return new ThreadPoolTaskScheduler();
    }
}
