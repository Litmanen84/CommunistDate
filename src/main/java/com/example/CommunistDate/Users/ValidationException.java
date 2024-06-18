package com.example.CommunistDate.Users;
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
