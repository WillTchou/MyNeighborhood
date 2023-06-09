package com.example.project.MyNeighborhood.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({UserStateException.class, RequestStateException.class,
            EmailAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ExceptionResponse> handleException(MyNeighborhoodStateException exception) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DocumentUnsupportedMediaTypeException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    ResponseEntity<ExceptionResponse> handleException(DocumentUnsupportedMediaTypeException exception) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({UserDoesNotExistException.class, GovernmentIdentityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ExceptionResponse> handleException(UserDoesNotExistException exception) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EnoughVolunteersException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseEntity<ExceptionResponse> handleException(EnoughVolunteersException exception) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ExceptionResponse> handleException(UsernameNotFoundException exception) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
