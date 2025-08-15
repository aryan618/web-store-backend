package com.aryan.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice //@ControllerAdvice in Spring Boot is basically a global “interceptor” for exceptions and controller-related logic.
//Think of it as a centralized error-handling and controller-helper system. If any exception gets raised from any of the controllers , then this particular controller will catch it
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex){
        // return  ResponseEntity.badRequest().body(ex.getMessage());

        var errors= new HashMap<String, String>();

        ex.getBindingResult().getFieldErrors().forEach((error)->{
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return  ResponseEntity.badRequest().body(errors);
    }
}
