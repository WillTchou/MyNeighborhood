package com.example.project.MyNeighborhood.exception;

public class RequestNotFoundException extends MyNeighborhoodStateException {
    private static final String CODE = "request.does.not.exist";
    private static final String MESSAGE = "This request doesn't exist";

    public RequestNotFoundException() {
        super(CODE, MESSAGE);
    }
}
