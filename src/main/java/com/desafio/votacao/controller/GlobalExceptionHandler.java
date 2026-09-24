package com.desafio.votacao.controller;

import com.desafio.votacao.exception.AlreadyVoteException;
import com.desafio.votacao.exception.ExceptionResponse;
import com.desafio.votacao.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("RestExceptionHandler.handleAllExceptions - [{}]", ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("RestExceptionHandler.handleIllegalArgumentException - [{}]", ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("RestExceptionHandler.handleResourceNotFoundException - [{}]", ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND.name(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(RuntimeException ex) {
        log.error("RestExceptionHandler.handleRuntimeException - [{}]", ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(AlreadyVoteException.class)
    public final ResponseEntity<Object> handleAlreadyVoteException(AlreadyVoteException ex) {
        log.error("RestExceptionHandler.handleRuntimeException - [{}]", ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.CONFLICT.name(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

}
