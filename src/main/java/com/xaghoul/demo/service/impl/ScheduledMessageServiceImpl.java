package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.exception.InvalidCronExpressionException;
import com.xaghoul.demo.model.ScheduledMessage;
import com.xaghoul.demo.repository.ScheduledMessageRepository;
import com.xaghoul.demo.service.ScheduledMessageService;
import com.xaghoul.demo.web.controller.MessageTemplateController;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@AllArgsConstructor
public class ScheduledMessageServiceImpl implements ScheduledMessageService {

    private final TaskScheduler scheduler;
    private final ScheduledMessageRepository repository;
    private final ScheduledMessageSender messageSender;
    private final ScheduledMessageModelAssembler assembler;
    private final Map<UUID, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    @Override
    public CollectionModel<EntityModel<ScheduledMessage>> getAll() {
        List<EntityModel<ScheduledMessage>> messages = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(messages,
                linkTo(methodOn(MessageTemplateController.class).getAllMessages()).withSelfRel());
    }

    @Override
    public void postMessage(ScheduledMessage message) {
        try {
            ScheduledFuture<?> task = scheduler.schedule(() -> sendMessage(message),
                    new CronTrigger(message.getCronExpression()));
            ScheduledMessage savedMessage = repository.save(message);
            scheduledTasks.put(savedMessage.getId(), task);
        } catch (IllegalArgumentException ex) {
            throw new InvalidCronExpressionException(message.getCronExpression());
        }

        sendMessage(message);
    }

    public void sendMessage(ScheduledMessage message) {
        messageSender.sendMessage(message);
    }

    public boolean stopSendingMessage(UUID messageId) {
        return messageSender.stopSendingMessage(messageId, repository, scheduledTasks);
    }
}
