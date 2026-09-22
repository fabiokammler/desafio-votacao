package com.desafio.votacao.exception;

import java.util.List;

public class ExceptionResponse {

    private String code;
    private String message;
    private String details;
    private List<String> argumentNotValid;

    public ExceptionResponse(String code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public ExceptionResponse(String code, List<String> messages) {
        this.code = code;
        this.argumentNotValid = messages;
    }

    public ExceptionResponse(String code) {
        this.code = code;
    }

    public ExceptionResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getArgumentNotValid() {
        return argumentNotValid;
    }

    public void setArgumentNotValid(List<String> argumentNotValid) {
        this.argumentNotValid = argumentNotValid;
    }
}
