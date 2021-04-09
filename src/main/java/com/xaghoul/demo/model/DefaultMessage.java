package com.xaghoul.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@MappedSuperclass
public class DefaultMessage {

    protected String message;

    @JsonCreator
    public DefaultMessage(@JsonProperty("message") String message) {
        this.message = message;
    }
}
