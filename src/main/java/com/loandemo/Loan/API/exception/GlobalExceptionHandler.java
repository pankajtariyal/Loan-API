package com.loandemo.Loan.API.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

/**
 * Global exception handler for the application.
 *
 * <p>This class handles all exceptions thrown across controllers
 * and returns a standardized {@link ErrorResponse}.
 *
 * @apiNote This ensures consistent error handling across all APIs.
 *
 * @since 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors from @Valid request body.
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException validException){

        String exception = validException.getBindingResult()
                .getFieldErrors().get(0)
                .getDefaultMessage();

        return new ResponseEntity<>(
                new ErrorResponse(exception,"Bad Request"),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle constraint violations (e.g., @RequestParam validation).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintException(ConstraintViolationException ex){
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), "BAD_REQUEST"),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle illegal arguments (manual validation).
     */
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ErrorResponse> handleIllegalException(IllegalAccessException ex){
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(),"Bad Request"),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle resource not found scenarios.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRunTimeException(RuntimeException ex){
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(),"Bad Request"),
                HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handle all unhandled exceptions (fallback).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        return new ResponseEntity<>(
                new ErrorResponse("Something went wrong.","INTERNAL SERVER ERROR"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
