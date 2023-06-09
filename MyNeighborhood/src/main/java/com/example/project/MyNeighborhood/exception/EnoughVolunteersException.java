package com.example.project.MyNeighborhood.exception;

public class EnoughVolunteersException extends MyNeighborhoodStateException{
    private static final String CODE = "enough.volunteers";
    private static final String MESSAGE = "There's enough volunteers for this request";
    public EnoughVolunteersException() {
        super(CODE, MESSAGE);
    }
}
