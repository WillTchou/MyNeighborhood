package com.example.project.MyNeighborhood.exception;

public class DocumentUnsupportedMediaTypeException extends MyNeighborhoodStateException {
    private static final String CODE = "document.unsupported.type";

    public DocumentUnsupportedMediaTypeException(String message) {
        super(CODE, message);
    }
}
