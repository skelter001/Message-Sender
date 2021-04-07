package com.xaghoul.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.CronTrigger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScheduledMessage extends DefaultMessage {

    @Id
    @GeneratedValue(generator = "UUID",
            strategy = GenerationType.IDENTITY)
    private UUID id;
    private String cronExpression;
    @ManyToOne
    private MessageTemplate template;

    @JsonCreator
    public ScheduledMessage(@JsonProperty("message") String message,
                            @JsonProperty("cronExpression") String cronExpression,
                            @JsonProperty("template") MessageTemplate template) {
        super(message);
        this.cronExpression = cronExpression;
        this.template = template;
    }
}
