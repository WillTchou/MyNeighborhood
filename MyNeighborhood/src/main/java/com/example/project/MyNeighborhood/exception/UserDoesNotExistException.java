package com.example.project.MyNeighborhood.exception;

public class UserDoesNotExistException extends MyNeighborhoodStateException {
    private static final String CODE = "user.does.not.exist";
    private static final String MESSAGE = "This user doesn't exist";

    public UserDoesNotExistException() {
        super(CODE, MESSAGE);
    }
}
