package com.example.CommunistDate.config;

public class ValidationException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ValidationException(ErrorResponse errorResponse) {
        super(errorResponse.toString());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
