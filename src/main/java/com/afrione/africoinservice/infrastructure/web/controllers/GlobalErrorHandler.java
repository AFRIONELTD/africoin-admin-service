package com.afrione.africoinservice.infrastructure.web.controllers;


import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.NoSuchElementException;


@Slf4j
@ControllerAdvice(basePackages = {"com.afrione.africoinservice.infrastructure.web.controllers"})
public class GlobalErrorHandler {

    private final ApplicationProperty applicationProperty;
    public GlobalErrorHandler(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    public ResponseEntity<ApiResponseJSON<String>> handleException(Exception exception) {
        logException(exception);
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>("Sorry, currently unable to process request at the moment.");
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //org.springframework.http.converter.

    @ExceptionHandler(value = {MissingServletRequestParameterException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiResponseJSON<String>> handleMissingServletRequestParameterException(Exception exception) {
        logException(exception);
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>(exception.getLocalizedMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponseJSON<String>> handleHttpMessageNotReadableException(Exception exception) {
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>("Invalid request.");
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MissingServletRequestPartException.class})
    public ResponseEntity<ApiResponseJSON<String>> handleMissingServletRequestPartException(Exception exception) {
        logException(exception);
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>("Request validation failure. Please check your request data.");
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponseJSON<Object>> handleUnauthorisedOperationException(AccessDeniedException exception) {
        logException(exception);
        return new ResponseEntity<>(new ApiResponseJSON<>("Sorry, you don't have the required privilege for the action."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponseJSON<String>> handleMethodArgumentNotValidExceptionException(MethodArgumentNotValidException exception) {
        String errorMessage = "Request validation failure. Please check your request data.";
        BindingResult result = exception.getBindingResult();
        FieldError fieldError = result.getFieldError();
        if(fieldError != null) {
            errorMessage = fieldError.getDefaultMessage();
        }
        log.info("error message: {}", errorMessage);
        logException(exception);
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>(errorMessage);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseJSON<String>> handleBadRequestException(BadRequestException e) {
        log.info("error message: {}", e.getMessage());
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>(e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessLogicConflictException.class)
    public ResponseEntity<ApiResponseJSON<String>> handleBusinessLogicConflictException(BusinessLogicConflictException e) {
        log.info("error message: {}", e.getMessage());
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>(e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    //

    @ExceptionHandler({NotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ApiResponseJSON<String>> handleNotFoundException(RuntimeException e) {
        log.info("error message: {}", e.getMessage());
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>(e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PreconditionRequiredException.class)
    public ResponseEntity<ApiResponseJSON<String>> handlePreconditionRequiredException(PreconditionRequiredException e) {
        log.info("error message: {}", e.getMessage());
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>(e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.PRECONDITION_REQUIRED);
    } //

    @ExceptionHandler(UnauthorisedAccessException.class)
    public ResponseEntity<ApiResponseJSON<String>> handleUnauthorisedExceptionException(UnauthorisedAccessException e) {
        log.info("error message: {}", e.getMessage());
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>(e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RequestForbiddenException.class)
    public ResponseEntity<ApiResponseJSON<String>> handleRequestForbiddenException(RequestForbiddenException e) {
        log.info("error message: {}", e.getMessage());
        ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>(e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }
    


    private void logException(Exception exception) {
        if(applicationProperty.isProductionEnvironment()) {
            // Sentry.captureException(exception);
            log.info("Exception: {}", ExceptionUtils.getStackTrace(exception));
        }else {
            log.info("Exception: {}", ExceptionUtils.getStackTrace(exception));
        }
    }
}
