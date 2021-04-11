package com.xaghoul.demo.config.initializer;

import com.xaghoul.demo.repository.ScheduledMessageRepository;
import com.xaghoul.demo.service.impl.ScheduledMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ScheduledMessagesConfig {

    private final ScheduledMessageRepository repository;
    private final ScheduledMessageServiceImpl service;

    @Autowired
    public ScheduledMessagesConfig(ScheduledMessageRepository repository, ScheduledMessageServiceImpl service) {
        this.repository = repository;
        this.service = service;
    }

    @PostConstruct
    void init() {
        repository
                .findAll()
                .forEach(service::postMessage);
    }
}
