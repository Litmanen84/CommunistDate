package com.example.CommunistDate.Users;

import java.util.List;

public class ErrorResponse {
    private String message;
    private List<FieldErrorResponse> fieldErrors;

    public ErrorResponse(String message, List<FieldErrorResponse> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldErrorResponse> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorResponse> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}

