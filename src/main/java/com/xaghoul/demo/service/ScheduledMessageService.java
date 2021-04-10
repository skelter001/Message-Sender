package com.xaghoul.demo.service;

import com.xaghoul.demo.model.ScheduledMessage;

import java.util.List;

public interface ScheduledMessageService {

    List<ScheduledMessage> postMessage(ScheduledMessage message);

    List<ScheduledMessage> getAll();
}
