package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.model.ScheduledMessage;
import com.xaghoul.demo.repository.ScheduledMessageRepository;
import com.xaghoul.demo.service.MessageTemplateScheduledService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MessageTemplateScheduledServiceImpl implements MessageTemplateScheduledService {

    private final TaskScheduler scheduler;
    private final ScheduledMessageRepository repository;

    @Override
    public List<ResponseEntity<Object>> postMessage(MessageTemplate messageTemplate, ScheduledMessage message) {
        repository.save(message);
        scheduler.schedule((Runnable) sendMessage(messageTemplate, message),
                new CronTrigger(message.getCronExpression()));
        return sendMessage(messageTemplate, message);
    }

    public List<ResponseEntity<Object>> sendMessage(MessageTemplate messageTemplate, ScheduledMessage message) {
        List<URL> urls = messageTemplate.getRecipients();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return urls.stream()
                .map(url -> restTemplate
                        .postForEntity(url.toString(), new HttpEntity<>(message, headers) , Object.class))
                .collect(Collectors.toList());
    }

    public boolean stopSendingMessage(UUID messageId) {
        ScheduledMessage message = repository.getOne(messageId);
        return Objects.requireNonNull(scheduler.schedule((Runnable) sendMessage(message.getTemplate(), message),
                new CronTrigger(message.getCronExpression()))).cancel(true);
    }
}
