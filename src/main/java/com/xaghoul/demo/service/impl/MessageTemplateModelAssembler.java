package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.web.controller.MessageTemplateController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MessageTemplateModelAssembler implements
        RepresentationModelAssembler<MessageTemplate, EntityModel<MessageTemplate>> {

    @Override
    public EntityModel<MessageTemplate> toModel(MessageTemplate messageTemplateEntity) {
        return EntityModel.of(messageTemplateEntity,
                linkTo(methodOn(MessageTemplateController.class).getById(messageTemplateEntity.getId())).withSelfRel(),
                linkTo(methodOn(MessageTemplateController.class).getAll()).withRel("templates"));
    }
}
