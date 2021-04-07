package com.xaghoul.demo.factory;

import com.xaghoul.demo.model.DefaultMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultMessageFactory implements MessageAbstractFactory {

    private String message;

    @Override
    public DefaultMessage createMessage() {
        return new DefaultMessage(message);
    }
}
