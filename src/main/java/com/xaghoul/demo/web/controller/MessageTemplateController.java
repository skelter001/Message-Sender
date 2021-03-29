package com.xaghoul.demo.web.controller;

import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.service.impl.MessageTemplateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/template")
public class MessageTemplateController {

    private final MessageTemplateServiceImpl service;

    @Autowired
    public MessageTemplateController(MessageTemplateServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{templateId}")
    public EntityModel<MessageTemplate> getById(@PathVariable UUID templateId) {
        return service.getById(templateId);
    }

    @GetMapping("/")
    public CollectionModel<EntityModel<MessageTemplate>> getAll() {
        return service.getAll();
    }

    @PostMapping("/add_template")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addTemplate(@RequestBody MessageTemplate messageTemplate) {
        return service.post(messageTemplate);
    }

    @PostMapping("/send_message")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> sendMessage(String templateName, String ... vars) {
        MessageTemplate messageTemplate = service.getByName(templateName);
        return new ResponseEntity<>(messageTemplate.createMessage(Arrays.asList(vars)), HttpStatus.OK);
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<?> putMessageTemplate(@RequestBody MessageTemplate newMessageTemplate,
                                                @PathVariable UUID templateId) {
        return service.put(newMessageTemplate, templateId);
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<?> delete(@PathVariable UUID templateId) {
        return service.delete(templateId);
    }
}
