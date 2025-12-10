package com.afrione.africoinservice.usecases.exceptions;

public class RequestForbiddenException extends RuntimeException {
    public RequestForbiddenException(String message){
        super(message);
    }
}
