package com.xaghoul.demo.factory;

import com.xaghoul.demo.model.DefaultMessage;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.model.ScheduledMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScheduledMessageFactory implements MessageAbstractFactory {

    private String message;
    private String cronTrigger;
    private MessageTemplate template;

    @Override
    public DefaultMessage createMessage() {
        return new ScheduledMessage(message, cronTrigger, template);
    }
}
