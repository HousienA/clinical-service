package com.fullstack.clinical_service.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) { super(message); }
}
