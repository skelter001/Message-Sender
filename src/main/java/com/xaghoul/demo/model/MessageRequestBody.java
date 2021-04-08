package com.xaghoul.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class MessageRequestBody {
    Map<String, String> variables = new HashMap<>();
    String cronExpression = null;

    @JsonCreator
    public MessageRequestBody(@JsonProperty("variables") Map<String, String> variables,
                              @JsonProperty("cronExpression") String cronExpression) {
        this.variables = variables;
        this.cronExpression = cronExpression;
    }
}
