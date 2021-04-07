package com.xaghoul.demo.service;

import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.model.ScheduledMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MessageTemplateScheduledService {

    List<ResponseEntity<Object>> postMessage(MessageTemplate messageTemplate, ScheduledMessage message);
}
