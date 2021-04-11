package com.xaghoul.demo.service;

import com.xaghoul.demo.model.DefaultMessage;
import com.xaghoul.demo.model.MessageTemplate;

public interface DefaultMessageService {
    void postMessage(MessageTemplate messageTemplate, DefaultMessage defaultMessage);
}
