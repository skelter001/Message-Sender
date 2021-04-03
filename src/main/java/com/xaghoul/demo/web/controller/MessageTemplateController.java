package com.xaghoul.demo.web.controller;

import com.xaghoul.demo.model.Message;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.service.impl.MessageTemplateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
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

    @GetMapping({"/", ""})
    public CollectionModel<EntityModel<MessageTemplate>> getAll() {
        return service.getAll();
    }

    @PostMapping("/add_template")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addTemplate(@RequestBody MessageTemplate messageTemplate) {
        return service.postTemplate(messageTemplate);
    }

    // TODO: 4/3/2021 ResponseEntity<Message> returning incorrect value
    @PostMapping(value = "/send_message/{templateName}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ResponseEntity<Object>> sendMessage(@RequestBody Map<String, String> variables, @PathVariable String templateName) {
        MessageTemplate template = service.getByName(templateName);
        Message msg = template.createMessage(variables);
        return service.postMessage(msg, template);
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<?> updateTemplate(@RequestBody MessageTemplate newMessageTemplate,
                                            @PathVariable UUID templateId) {
        return service.put(newMessageTemplate, templateId);
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<?> deleteTemplate(@PathVariable UUID templateId) {
        return service.delete(templateId);
    }
}
