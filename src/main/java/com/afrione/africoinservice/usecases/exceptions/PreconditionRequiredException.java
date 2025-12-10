package com.afrione.africoinservice.usecases.exceptions;

public class PreconditionRequiredException extends RuntimeException{
    public PreconditionRequiredException(String message){
        super(message);
    }
}
