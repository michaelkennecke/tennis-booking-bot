package com.example.blauhofbot.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(String message, HttpStatus httpStatus, LocalDateTime timeStamp) {}
