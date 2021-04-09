package com.xaghoul.demo.web.controller;

import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.service.impl.ScheduledMessageService;
import com.xaghoul.demo.service.impl.MessageTemplateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class DefaultMessageTemplateControllerTest {

    private final MessageTemplateServiceImpl messageTemplateService = Mockito.mock(MessageTemplateServiceImpl.class);
    private final ScheduledMessageService messageScheduledService = Mockito.mock(ScheduledMessageService.class);

    @Test
    void whenSaveTemplate_getByIdValid() throws MalformedURLException {
        MessageTemplateController controller = new MessageTemplateController(messageTemplateService, messageScheduledService);
        UUID id = UUID.randomUUID();
        EntityModel<MessageTemplate> expectedValue = EntityModel.of(new MessageTemplate(
                id, "template1", "Template message",
                Collections.singletonList(new URL("https://httpbin.org/post"))));

        Mockito.when(messageTemplateService.getById(id)).thenReturn(expectedValue);

        EntityModel<MessageTemplate> actualValue = controller.getById(id);

        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    void whenSaveListOfTemplates_thenGetValidList() throws MalformedURLException {
        MessageTemplateController controller = new MessageTemplateController(messageTemplateService, messageScheduledService);
        UUID id = UUID.randomUUID();
        EntityModel<MessageTemplate> template1 = EntityModel.of(new MessageTemplate(
                id, "template1", "Template message",
                Collections.singletonList(new URL("https://httpbin.org/post"))));
        EntityModel<MessageTemplate> template2 = EntityModel.of(new MessageTemplate(
                id, "template2", "Template message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                              new URL("https://postman-echo.com/post"))));

        CollectionModel<EntityModel<MessageTemplate>> expected = CollectionModel.of(List.of(template1, template2));

        Mockito.when(messageTemplateService.getAll()).thenReturn(expected);

        CollectionModel<EntityModel<MessageTemplate>> actual = controller.getAllTemplates();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenSendMessage_thenValidRequest() throws MalformedURLException {
        MessageTemplateController controller = new MessageTemplateController(messageTemplateService, messageScheduledService);
        UUID id = UUID.randomUUID();
        EntityModel<MessageTemplate> template2 = EntityModel.of(new MessageTemplate(
                id, "template2", "Template message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post"))));


    }

    @Test
    void updateTemplate() {
    }

    @Test
    void deleteTemplate() {
    }
}