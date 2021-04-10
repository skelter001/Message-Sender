package com.xaghoul.demo.service;


import com.xaghoul.demo.model.MessageTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface TemplateMessageService {

    ResponseEntity<?> postTemplate(MessageTemplate messageTemplate);

    CollectionModel<EntityModel<MessageTemplate>> getAll();

    EntityModel<MessageTemplate> getById(UUID id);

    ResponseEntity<?> putTemplate(MessageTemplate newMessageTemplate, UUID id);

    ResponseEntity<?> deleteTemplate(UUID id);

    MessageTemplate getByName(String name);
}
