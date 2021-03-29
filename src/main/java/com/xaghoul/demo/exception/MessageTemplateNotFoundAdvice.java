package com.xaghoul.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MessageTemplateNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(MessageTemplateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String messageTemplateNotFoundHandler(MessageTemplateNotFoundException ex) {
        return ex.getMessage();
    }
}
