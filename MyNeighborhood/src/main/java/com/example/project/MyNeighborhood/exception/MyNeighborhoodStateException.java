package com.example.project.MyNeighborhood.exception;

import lombok.Getter;

@Getter
public class MyNeighborhoodStateException extends RuntimeException {
    protected final String code;
    protected final String message;

    public MyNeighborhoodStateException(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
