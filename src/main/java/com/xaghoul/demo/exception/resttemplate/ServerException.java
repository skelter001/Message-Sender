package com.xaghoul.demo.exception.resttemplate;

public class ServerException extends RuntimeException {
    public ServerException() {
        super("The application has encountered an unknown error.\n" +
                "It doesn't appear to have affected your data, but our technical staff have been " +
                "automatically notified and will be looking into this with the utmost urgency.");
    }
}
