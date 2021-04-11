package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.model.ScheduledMessage;
import com.xaghoul.demo.web.controller.MessageTemplateController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ScheduledMessageModelAssembler
        implements RepresentationModelAssembler<ScheduledMessage, EntityModel<ScheduledMessage>> {

    @Override
    public EntityModel<ScheduledMessage> toModel(ScheduledMessage scheduledMessageEntity) {
        return EntityModel.of(scheduledMessageEntity,
                linkTo(methodOn(MessageTemplateController.class).getAllMessages()).withRel("messages"));
    }
}
