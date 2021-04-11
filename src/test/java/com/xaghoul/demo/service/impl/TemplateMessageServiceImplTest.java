package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.exception.MessageTemplateNotFoundException;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.repository.MessageTemplateRepository;
import com.xaghoul.demo.web.controller.MessageTemplateController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SpringBootTest
class TemplateMessageServiceImplTest {

    @Autowired
    private TemplateMessageServiceImpl templateMessageService;

    @Autowired
    @MockBean
    private MessageTemplateRepository repository;

    @Autowired
    @MockBean
    private MessageTemplateModelAssembler assembler;

    @Test
    void getAllTest() throws MalformedURLException {
        MessageTemplate template1 = new MessageTemplate(
                UUID.randomUUID(), "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));
        MessageTemplate template2 = new MessageTemplate(
                UUID.randomUUID(), "$$$", "Today is $year$ year",
                Collections.singletonList(new URL("https://httpbin.org/post")));

        Mockito.when(assembler.toModel(template1)).thenReturn(EntityModel.of(template1,
                linkTo(methodOn(MessageTemplateController.class).getById(template1.getId())).withSelfRel(),
                linkTo(methodOn(MessageTemplateController.class).getAllTemplates()).withRel("templates")));

        Mockito.when(assembler.toModel(template2)).thenReturn(EntityModel.of(template2,
                linkTo(methodOn(MessageTemplateController.class).getById(template2.getId())).withSelfRel(),
                linkTo(methodOn(MessageTemplateController.class).getAllTemplates()).withRel("templates")));

        Mockito.when(repository.save(template1)).thenReturn(template1);
        Mockito.when(repository.save(template2)).thenReturn(template2);
        Mockito.when(repository.findAll()).thenReturn(List.of(template1, template2));

        templateMessageService.postTemplate(template1);
        templateMessageService.postTemplate(template2);

        CollectionModel<EntityModel<MessageTemplate>> expected = CollectionModel.of(repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()))
                .add(linkTo(methodOn(MessageTemplateController.class).getAllTemplates()).withSelfRel());
        Assertions.assertEquals(expected.toString(), templateMessageService.getAll().toString());
    }

    @Test
    void getByIdTest() throws MalformedURLException {
        UUID id = UUID.randomUUID();
        MessageTemplate template1 = new MessageTemplate(
                id, "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));

        Mockito.when(repository.save(template1)).thenReturn(template1);
        Mockito.when(repository.findById(id)).thenReturn(java.util.Optional.of(template1));

        Mockito.when(assembler.toModel(template1)).thenReturn(EntityModel.of(template1,
                linkTo(methodOn(MessageTemplateController.class).getById(template1.getId())).withSelfRel(),
                linkTo(methodOn(MessageTemplateController.class).getAllTemplates()).withRel("templates")));

        templateMessageService.postTemplate(template1);

        EntityModel<MessageTemplate> expected = assembler.toModel(template1);

        Assertions.assertEquals(expected.toString(), templateMessageService.getById(id).toString());
    }

    @Test
    void postTemplateTest() throws MalformedURLException {
        UUID id = UUID.randomUUID();
        MessageTemplate template1 = new MessageTemplate(
                id, "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));

        Mockito.when(repository.save(template1)).thenReturn(template1);

        Mockito.when(assembler.toModel(template1)).thenReturn(EntityModel.of(template1,
                linkTo(methodOn(MessageTemplateController.class).getById(template1.getId())).withSelfRel(),
                linkTo(methodOn(MessageTemplateController.class).getAllTemplates()).withRel("templates")));

        EntityModel<MessageTemplate> expectedModel = assembler.toModel(template1);

        ResponseEntity<?> expected = ResponseEntity.created(expectedModel
                .getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(expectedModel);

        Assertions.assertEquals(expected, templateMessageService.postTemplate(template1));
    }

    @Test
    void putTemplateTest() throws MalformedURLException {
        UUID id = UUID.randomUUID();
        MessageTemplate template1 = new MessageTemplate(
                id, "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));

        Mockito.when(repository.save(template1)).thenReturn(template1);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(template1));

        Mockito.when(assembler.toModel(template1)).thenReturn(EntityModel.of(template1,
                linkTo(methodOn(MessageTemplateController.class).getById(template1.getId())).withSelfRel(),
                linkTo(methodOn(MessageTemplateController.class).getAllTemplates()).withRel("templates")));

        templateMessageService.postTemplate(template1);

        template1.setName("template2");

        EntityModel<MessageTemplate> expectedModel = assembler.toModel(template1);

        ResponseEntity<?> expected = ResponseEntity.created(expectedModel
                .getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(expectedModel);

        Assertions.assertEquals(expected, templateMessageService.putTemplate(template1, template1.getId()));
    }

    @Test
    void deleteTemplateTest() throws MalformedURLException {
        UUID id = UUID.randomUUID();
        MessageTemplate template1 = new MessageTemplate(
                id, "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));

        Mockito.when(repository.save(template1)).thenReturn(template1);

        Mockito.when(assembler.toModel(template1)).thenReturn(EntityModel.of(template1,
                linkTo(methodOn(MessageTemplateController.class).getById(template1.getId())).withSelfRel(),
                linkTo(methodOn(MessageTemplateController.class).getAllTemplates()).withRel("templates")));

        templateMessageService.postTemplate(template1);

        Assertions.assertEquals(ResponseEntity.noContent().build(),
                templateMessageService.deleteTemplate(template1.getId()));
        Assertions.assertThrows(MessageTemplateNotFoundException.class, () ->
                templateMessageService.getByName(template1.getName()));
    }

    @Test
    void getByNameTest() throws MalformedURLException {
        UUID id = UUID.randomUUID();
        MessageTemplate template1 = new MessageTemplate(
                id, "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));

        Mockito.when(repository.save(template1)).thenReturn(template1);
        Mockito.when(repository.getByName(template1.getName())).thenReturn(Optional.of(template1));

        Mockito.when(assembler.toModel(template1)).thenReturn(EntityModel.of(template1,
                linkTo(methodOn(MessageTemplateController.class).getById(template1.getId())).withSelfRel(),
                linkTo(methodOn(MessageTemplateController.class).getAllTemplates()).withRel("templates")));

        templateMessageService.postTemplate(template1);

        Assertions.assertEquals(template1, templateMessageService.getByName(template1.getName()));
    }
}