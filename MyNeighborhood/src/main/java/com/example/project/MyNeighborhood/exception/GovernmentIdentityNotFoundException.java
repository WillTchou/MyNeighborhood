package com.example.project.MyNeighborhood.exception;

public class GovernmentIdentityNotFoundException extends MyNeighborhoodStateException {
    private static final String CODE = "government.identity.does.not.exist";
    private static final String MESSAGE = "This government identity doesn't exist";

    public GovernmentIdentityNotFoundException() {
        super(CODE, MESSAGE);
    }
}
