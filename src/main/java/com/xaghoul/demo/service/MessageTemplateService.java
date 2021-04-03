package com.xaghoul.demo.service;


import com.xaghoul.demo.model.Message;
import com.xaghoul.demo.model.MessageTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface MessageTemplateService {

    ResponseEntity<?> postTemplate(MessageTemplate messageTemplate);

    CollectionModel<EntityModel<MessageTemplate>> getAll();

    EntityModel<MessageTemplate> getById(UUID id);

    ResponseEntity<?> put(MessageTemplate newMessageTemplate, UUID id);

    ResponseEntity<?> delete(UUID id);

    List<ResponseEntity<Object>> postMessage(Message message, MessageTemplate messageTemplate);

    MessageTemplate getByName(String name);
}
