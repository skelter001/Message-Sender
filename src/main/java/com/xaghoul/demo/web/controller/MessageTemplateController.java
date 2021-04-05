package com.xaghoul.demo.web.controller;

import com.xaghoul.demo.model.Message;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.service.impl.MessageTemplateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/template")
@EnableScheduling
@Slf4j
public class MessageTemplateController {

    private final MessageTemplateServiceImpl service;
    private final Map<MessageTemplate, List<Message>> periodMessages = new HashMap<>();

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
        Message message = template.createMessage(variables);
        if(!periodMessages.containsKey(template))
            periodMessages.put(template, new ArrayList<>(){{add(message);}});
        if(periodMessages.get(template).contains(message))
            periodMessages.get(template).add(message);
        return service.postMessage(template, message);
    }

    @PostMapping()
    @Scheduled(cron = "0 0/10 * * * *")
    public void sendMessagePeriodically() {
        periodMessages.forEach((template, messages) ->
                messages.forEach(message ->  service.postMessage(template, message)));
        log.info("Messages sent");
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
