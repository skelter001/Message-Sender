package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.model.ScheduledMessage;
import com.xaghoul.demo.repository.ScheduledMessageRepository;
import com.xaghoul.demo.web.controller.MessageTemplateController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SpringBootTest
class ScheduledMessageServiceImplTest {
    @Autowired
    private ScheduledMessageServiceImpl scheduledMessageService;
    @MockBean
    private ScheduledMessageRepository repository;
    @MockBean
    private ScheduledMessageModelAssembler assembler;
    @MockBean
    private ScheduledMessageSender messageSender;


    @Test
    void getAll() throws MalformedURLException {
        MessageTemplate template1 = new MessageTemplate(
                UUID.randomUUID(), "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));

        String cronExpression_everyTenMinutes = "0 */10 * ? * *";
        Map<String, String> variables = Map.of("test", "123");
        ScheduledMessage msg1 = new ScheduledMessage(UUID.randomUUID(),
                cronExpression_everyTenMinutes, template1);
        msg1.setMessage(template1.createMessage(variables));

        String cronExpression_everyMinute = "0 * * ? * *";
        ScheduledMessage msg2 = new ScheduledMessage(UUID.randomUUID(),
                cronExpression_everyMinute, template1);
        msg2.setMessage(template1.createMessage(variables));

        Mockito.when(repository.save(msg1)).thenReturn(msg1);
        Mockito.when(repository.save(msg2)).thenReturn(msg2);
        Mockito.when(repository.findAll()).thenReturn(List.of(msg1, msg2));

        Mockito.when(assembler.toModel(msg1)).thenReturn(EntityModel.of(msg1,
                linkTo(methodOn(MessageTemplateController.class).getAllMessages()).withRel("messages")));
        Mockito.when(assembler.toModel(msg2)).thenReturn(EntityModel.of(msg2,
                linkTo(methodOn(MessageTemplateController.class).getAllMessages()).withRel("messages")));

        scheduledMessageService.postMessage(msg1);
        scheduledMessageService.postMessage(msg2);

        CollectionModel<EntityModel<ScheduledMessage>> expected = CollectionModel.of(repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()))
                .add(linkTo(methodOn(MessageTemplateController.class).getAllMessages()).withSelfRel());

        Assertions.assertEquals(expected, scheduledMessageService.getAll());

        scheduledMessageService.stopSendingMessage(msg1.getId());
        scheduledMessageService.stopSendingMessage(msg2.getId());
    }

    @Test
    void postMessageTest() throws MalformedURLException, InterruptedException {
        MessageTemplate template1 = new MessageTemplate(
                UUID.randomUUID(), "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));

        String cronExpression_everyTwoSeconds = "* * * * * *";
        Map<String, String> variables = Map.of("test", "123");
        ScheduledMessage msg1 = new ScheduledMessage(UUID.randomUUID(),
                cronExpression_everyTwoSeconds, template1);
        msg1.setMessage(template1.createMessage(variables));

        Mockito.when(repository.save(msg1)).thenReturn(msg1);

        Mockito.when(assembler.toModel(msg1)).thenReturn(EntityModel.of(msg1,
                linkTo(methodOn(MessageTemplateController.class).getAllMessages()).withRel("messages")));

        scheduledMessageService.postMessage(msg1);

        Thread.sleep(1000);

        Mockito.verify(messageSender, Mockito.times(3))
                .sendMessage(ArgumentMatchers.eq(msg1));
    }

    @Test
    void stopSendingMessageTest() throws MalformedURLException {
        MessageTemplate template1 = new MessageTemplate(
                UUID.randomUUID(), "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));

        String cronExpression_everyTwoSeconds = "* * * * * *";
        Map<String, String> variables = Map.of("test", "123");
        ScheduledMessage msg1 = new ScheduledMessage(UUID.randomUUID(),
                cronExpression_everyTwoSeconds, template1);
        msg1.setMessage(template1.createMessage(variables));

        Mockito.when(repository.save(msg1)).thenReturn(msg1);

        Mockito.when(assembler.toModel(msg1)).thenReturn(EntityModel.of(msg1,
                linkTo(methodOn(MessageTemplateController.class).getAllMessages()).withRel("messages")));

        scheduledMessageService.postMessage(msg1);

        scheduledMessageService.stopSendingMessage(msg1.getId());

        CollectionModel<EntityModel<ScheduledMessage>> expected = CollectionModel.of(repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()))
                .add(linkTo(methodOn(MessageTemplateController.class).getAllMessages()).withSelfRel());

        Assertions.assertEquals(expected, scheduledMessageService.getAll());
    }
}