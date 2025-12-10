package com.afrione.africoinservice.usecases.exceptions;

/**
 * Exception thrown when business logic validation fails
 * Created for AdminOrder functionality
 */
public class BusinessLogicException extends RuntimeException {

    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}