package com.xaghoul.demo.service;

import com.xaghoul.demo.model.ScheduledMessage;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface ScheduledMessageService {

    void postMessage(ScheduledMessage message);

    CollectionModel<EntityModel<ScheduledMessage>> getAll();
}
