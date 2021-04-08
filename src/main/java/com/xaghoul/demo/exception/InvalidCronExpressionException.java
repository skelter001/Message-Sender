package com.xaghoul.demo.exception;

public class InvalidCronExpressionException extends RuntimeException {

    public InvalidCronExpressionException(String cronExpression) {
        super(cronExpression + " is a wrong CronExpression");
    }
}
