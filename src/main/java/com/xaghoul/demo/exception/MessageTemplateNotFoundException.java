package com.xaghoul.demo.exception;

import java.util.UUID;

public class MessageTemplateNotFoundException extends RuntimeException {

    public MessageTemplateNotFoundException(UUID id) {
        super("Message template with " + id.toString() + " id not found");
    }

    public MessageTemplateNotFoundException(String name) {
        super("Message template with " + name + " name not found");
    }

    public MessageTemplateNotFoundException(UUID id, Throwable e) {
        super("Message template with " + id.toString() + " id not found", e);
    }
}
