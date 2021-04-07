package com.xaghoul.demo.web.controller;

import com.xaghoul.demo.factory.DefaultMessageFactory;
import com.xaghoul.demo.factory.MessageFactory;
import com.xaghoul.demo.factory.ScheduledMessageFactory;
import com.xaghoul.demo.model.DefaultMessage;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.model.ScheduledMessage;
import com.xaghoul.demo.service.impl.MessageTemplateScheduledServiceImpl;
import com.xaghoul.demo.service.impl.MessageTemplateServiceImpl;
import lombok.AllArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/template")
@AllArgsConstructor
public class MessageTemplateController {

    private final MessageTemplateServiceImpl defaultService;
    private final MessageTemplateScheduledServiceImpl scheduledService;

    @GetMapping("/{templateId}")
    public EntityModel<MessageTemplate> getById(@PathVariable UUID templateId) {
        return defaultService.getById(templateId);
    }

    @GetMapping({"/", ""})
    public CollectionModel<EntityModel<MessageTemplate>> getAll() {
        return defaultService.getAll();
    }

    @PostMapping("/add_template")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addTemplate(@RequestBody MessageTemplate messageTemplate) {
        return defaultService.postTemplate(messageTemplate);
    }

    // TODO: 4/3/2021 ResponseEntity<Message> returning incorrect value
    @PostMapping(value = "/send_message/{templateName}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ResponseEntity<Object>> sendMessage(@RequestParam(required = false) String sendType,
                                                    @RequestBody Map<String, String> variables,
                                                    @PathVariable String templateName) {
        MessageTemplate template = defaultService.getByName(templateName);
        DefaultMessage message;
        if(sendType.equals("scheduled") && variables.containsKey("cronExpression")) {
            message = MessageFactory.getMessage(
                    new ScheduledMessageFactory(template.createMessage(variables),
                            variables.get("cronExpression"), template));
            return scheduledService.postMessage(template, (ScheduledMessage) message);
        }
        else {
            message = MessageFactory.getMessage(
                    new DefaultMessageFactory(template.createMessage(variables)));
            return defaultService.postMessage(template, message);
        }
    }

    @PostMapping("/send_message/cancel/{messageId}")
    public HttpStatus cancelSendingMessage(@PathVariable UUID messageId) {
        if (scheduledService.stopSendingMessage(messageId))
            return HttpStatus.ACCEPTED;
        else
            return HttpStatus.NO_CONTENT;
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<?> updateTemplate(@RequestBody MessageTemplate newMessageTemplate,
                                            @PathVariable UUID templateId) {
        return defaultService.putTemplate(newMessageTemplate, templateId);
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<?> deleteTemplate(@PathVariable UUID templateId) {
        return defaultService.deleteTemplate(templateId);
    }
}
