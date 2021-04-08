package com.xaghoul.demo.service;

import com.xaghoul.demo.model.ScheduledMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MessageTemplateScheduledService {

    List<ResponseEntity<Object>> postMessage(ScheduledMessage message);
}
