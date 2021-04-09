package com.xaghoul.demo.exception;

import java.util.UUID;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(UUID id) {
        super("Message with " + id.toString() + " id not found");
    }
}
