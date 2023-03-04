package com.example.blauhofbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleApiRequestException(NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                httpStatus,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
