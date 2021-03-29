package com.xaghoul.demo.service;


import com.xaghoul.demo.model.MessageTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface MessageTemplateService {

    ResponseEntity<?> post(MessageTemplate messageTemplate);

    CollectionModel<EntityModel<MessageTemplate>> getAll();

    EntityModel<MessageTemplate> getById(UUID id);

    ResponseEntity<?> put(MessageTemplate newMessageTemplate, UUID id);

    ResponseEntity<?> delete(UUID id);

    MessageTemplate getByName(String name);
}
