package com.xaghoul.demo.service.impl;

import com.xaghoul.demo.model.DefaultMessage;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.model.ScheduledMessage;
import com.xaghoul.demo.service.DefaultMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class DefaultMessageServiceImpl implements DefaultMessageService {

    private final RestTemplate restTemplate;

    @Override
    public List<ScheduledMessage> postMessage(MessageTemplate messageTemplate, DefaultMessage defaultMessage) {
        List<URL> urls = messageTemplate.getRecipients();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        log.info("Message {} is sending to {} recipients", defaultMessage.getMessage(), urls.toString());

        return urls.stream()
                .map(url -> restTemplate
                        .postForObject(url.toString(), new HttpEntity<>(defaultMessage, headers) , ScheduledMessage.class))
                .collect(Collectors.toList());
    }
}
