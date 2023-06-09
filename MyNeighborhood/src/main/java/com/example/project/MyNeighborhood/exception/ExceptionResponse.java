package com.example.project.MyNeighborhood.exception;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ExceptionResponse {
    String code;
    String message;
}
