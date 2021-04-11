package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.exception.MessageNotFoundException;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.model.ScheduledMessage;
import com.xaghoul.demo.repository.ScheduledMessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
@AllArgsConstructor
public class ScheduledMessageSender {

    private final RestTemplate restTemplate;

    public void sendMessage(ScheduledMessage message) {
        MessageTemplate messageTemplate = message.getTemplate();
        List<URL> urls = messageTemplate.getRecipients();

        log.info("Message {} is sending to {} recipients", message.getMessage(), message.getTemplate().getRecipients());
        try {
            urls.forEach(url -> restTemplate
                    .postForObject(url.toString(), new HttpEntity<>(message), ScheduledMessage.class));
        } catch (HttpStatusCodeException se) {
            log.debug(se.getResponseBodyAsString());
        }
    }

    public boolean stopSendingMessage(UUID messageId,
                                      ScheduledMessageRepository repository,
                                      Map<UUID, ScheduledFuture<?>> scheduledTasks) {

        ScheduledMessage message = repository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        log.info("Message {} sending is stopping", message.getMessage());

        repository.delete(message);

        if (scheduledTasks.containsKey(messageId))
            return scheduledTasks.get(messageId).cancel(true);
        else
            throw new MessageNotFoundException(messageId);
    }
}
