package com.xaghoul.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplate {

    @Id
    @GeneratedValue(generator = "UUID",
                    strategy = GenerationType.IDENTITY)
    UUID id;
    String name;
    String substitutionValue;
    @ElementCollection
    List<String> recipients = new ArrayList<>();

    public Message createMessage(List<String> variables) {
        return new Message(name, variables);
    }
}
