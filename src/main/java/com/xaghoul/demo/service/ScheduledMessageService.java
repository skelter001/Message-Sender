package com.xaghoul.demo.service;

import com.xaghoul.demo.model.ScheduledMessage;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.UUID;

public interface ScheduledMessageService {

    void postMessage(ScheduledMessage message);

    CollectionModel<EntityModel<ScheduledMessage>> getAll();

    boolean stopSendingMessage(UUID messageId);
}
