package com.xaghoul.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Message {
    String message;

    @JsonCreator
    public Message(@JsonProperty String message) {
        this.message = message;
    }
}
