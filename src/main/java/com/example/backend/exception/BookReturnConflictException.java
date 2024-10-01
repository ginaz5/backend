package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookReturnConflictException extends RuntimeException {
    public BookReturnConflictException(String message) {
        super(message);
    }
}