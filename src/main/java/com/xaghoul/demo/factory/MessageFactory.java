package com.xaghoul.demo.factory;

import com.xaghoul.demo.model.DefaultMessage;

public class MessageFactory {
    public static DefaultMessage getMessage(MessageAbstractFactory factory) {
        return factory.createMessage();
    }
}
