package com.xaghoul.demo.service;

import com.xaghoul.demo.model.DefaultMessage;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.model.ScheduledMessage;

import java.util.List;

public interface DefaultMessageService {
    List<ScheduledMessage> postMessage(MessageTemplate messageTemplate, DefaultMessage defaultMessage);
}
