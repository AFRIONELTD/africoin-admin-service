package com.afrione.africoinservice.usecases.exceptions;

public class BusinessLogicConflictException extends RuntimeException{
    public BusinessLogicConflictException(String message){
        super(message);
    }
}
