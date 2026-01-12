package com.afrione.africoinservice.usecases.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class APIRequestException extends RuntimeException {
    @Getter
    private final HttpStatus httpStatus;
    public APIRequestException(HttpStatus httpStatus, String message){
        super(message);
        this.httpStatus = httpStatus;
    }
}
