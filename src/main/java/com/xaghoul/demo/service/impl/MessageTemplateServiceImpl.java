package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.exception.MessageTemplateNotFoundException;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.repository.MessageTemplateRepository;
import com.xaghoul.demo.service.MessageTemplateService;
import com.xaghoul.demo.web.controller.MessageTemplateController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private final MessageTemplateRepository repository;
    private final MessageTemplateModelAssembler assembler;

    @Autowired
    public MessageTemplateServiceImpl(MessageTemplateRepository repository, MessageTemplateModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public CollectionModel<EntityModel<MessageTemplate>> getAll() {
        List<EntityModel<MessageTemplate>> templates = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(templates,
                linkTo(methodOn(MessageTemplateController.class).getAll()).withSelfRel());
    }

    @Override
    public EntityModel<MessageTemplate> getById(UUID id) {
        MessageTemplate messageTemplate = repository.findById(id)
                .orElseThrow(() -> new MessageTemplateNotFoundException(id));

        return assembler.toModel(messageTemplate);
    }

    @Override
    public ResponseEntity<?> post(@RequestBody MessageTemplate newMessageTemplate) {
        EntityModel<MessageTemplate> messageTemplateModel = assembler.toModel(repository.save(newMessageTemplate));

        return ResponseEntity
                .created(messageTemplateModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(messageTemplateModel);
    }

    @Override
    public ResponseEntity<?> put(@RequestBody MessageTemplate newMessageTemplate, @PathVariable UUID id) {
        MessageTemplate updatedMessageTemplate = repository.findById(id)
                .map(messageTemplate -> {
                    messageTemplate.setName(newMessageTemplate.getName());
                    messageTemplate.setTemplate(newMessageTemplate.getTemplate());
                    messageTemplate.setRecipients(newMessageTemplate.getRecipients());
                    return repository.save(messageTemplate);
                })
                .orElseGet(() -> {
                    newMessageTemplate.setId(id);
                    return repository.save(newMessageTemplate);
                });

        EntityModel<MessageTemplate> messageTemplateModel = assembler.toModel(updatedMessageTemplate);

        return ResponseEntity
                .created(messageTemplateModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(messageTemplateModel);
    }

    @Override
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public MessageTemplate getByName(String name) {
        if(repository.getByName(name).isPresent())
            return repository.getByName(name).get();
        else
            throw new MessageTemplateNotFoundException(name);
    }
}
