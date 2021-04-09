package com.xaghoul.demo.config.initializer;

import com.xaghoul.demo.model.ScheduledMessage;
import com.xaghoul.demo.repository.ScheduledMessageRepository;
import com.xaghoul.demo.service.impl.ScheduledMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class ScheduledMessagesConfig {

    private final ScheduledMessageRepository repository;
    private final ScheduledMessageService service;

    @Autowired
    public ScheduledMessagesConfig(ScheduledMessageRepository repository, ScheduledMessageService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostConstruct
    void init() {
        List<ScheduledMessage> messages = repository.findAll();
        messages.forEach(service::postMessage);
    }
}
