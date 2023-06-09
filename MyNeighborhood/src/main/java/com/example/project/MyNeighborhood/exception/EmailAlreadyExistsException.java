package com.example.project.MyNeighborhood.exception;

public class EmailAlreadyExistsException extends MyNeighborhoodStateException{
    private static final String CODE = "email.already.exist";

    public EmailAlreadyExistsException(String message) {
        super(CODE, message);
    }
}
