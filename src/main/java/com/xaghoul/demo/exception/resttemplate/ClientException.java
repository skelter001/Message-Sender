package com.xaghoul.demo.exception.resttemplate;

public class ClientException extends RuntimeException {
    public ClientException() {
        super("Your client has issued a malformed or illegal request");
    }
}
