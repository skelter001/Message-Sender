package com.xaghoul.demo.model;

import lombok.Value;

import java.util.List;

@Value
public class Message {
    String templateName;
    List<String> variables;
}
