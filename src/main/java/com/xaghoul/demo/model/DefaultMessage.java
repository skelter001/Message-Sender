package com.xaghoul.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DefaultMessage {

    protected String message;

    @JsonCreator
    public DefaultMessage(@JsonProperty("message") String message) {
        this.message = message;
    }
}
