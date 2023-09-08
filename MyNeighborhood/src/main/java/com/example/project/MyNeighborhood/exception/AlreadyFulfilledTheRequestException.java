package com.example.project.MyNeighborhood.exception;

public class AlreadyFulfilledTheRequestException extends MyNeighborhoodStateException {

    private static final String CODE = "has.already.fulfilled.request";
    private static final String MESSAGE = "This user has already fulfilled the request";

    public AlreadyFulfilledTheRequestException() {
        super(CODE, MESSAGE);
    }
}
