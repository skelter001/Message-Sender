package com.xaghoul.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Map;

@Value
public class Message {
    String templateName;
    Map<String, String> variables;

    @JsonCreator
    public Message(@JsonProperty String templateName,
                   @JsonProperty Map<String, String> variables) {
        this.templateName = templateName;
        this.variables = variables;
    }
}
