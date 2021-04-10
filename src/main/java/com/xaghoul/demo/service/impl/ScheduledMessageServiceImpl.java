package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.exception.InvalidCronExpressionException;
import com.xaghoul.demo.exception.MessageNotFoundException;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.model.ScheduledMessage;
import com.xaghoul.demo.repository.ScheduledMessageRepository;
import com.xaghoul.demo.service.ScheduledMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduledMessageServiceImpl implements ScheduledMessageService {

    private final TaskScheduler scheduler;
    private final ScheduledMessageRepository repository;
    private final RestTemplate restTemplate;
    private final Map<UUID, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    @Override
    public List<ScheduledMessage> getAll() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public List<ScheduledMessage> postMessage(ScheduledMessage message) {

        try {
            ScheduledFuture<?> task = scheduler.schedule(() -> sendMessage(message),
                    new CronTrigger(message.getCronExpression()));
            ScheduledMessage savedMessage = repository.save(message);
            scheduledTasks.put(savedMessage.getId(), task);
        } catch (IllegalArgumentException ex) {
            throw new InvalidCronExpressionException(message.getCronExpression());
        }

        return sendMessage(message);
    }

    public List<ScheduledMessage> sendMessage(ScheduledMessage message) {
        MessageTemplate messageTemplate = message.getTemplate();
        List<URL> urls = messageTemplate.getRecipients();

        log.info("Message {} is sending to {} recipients", message.getMessage(), message.getTemplate().getRecipients());
        try {
            return urls.stream()
                    .map(url -> restTemplate
                            .postForObject(url.toString(), new HttpEntity<>(message), ScheduledMessage.class))
                    .collect(Collectors.toList());
        } catch (HttpStatusCodeException se) {
            log.debug(se.getResponseBodyAsString());
        }
        return null;
    }

    public boolean stopSendingMessage(UUID messageId) {
        ScheduledMessage message = repository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        log.info("Message {} sending is stopping", message.getMessage());
        repository.delete(message);
        if(scheduledTasks.containsKey(messageId))
            return scheduledTasks.get(messageId).cancel(true);
        else
            throw new MessageNotFoundException(messageId);
    }
}
