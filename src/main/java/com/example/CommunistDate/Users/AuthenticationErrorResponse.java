package com.example.CommunistDate.Users;

public class AuthenticationErrorResponse {
    private String errorMessage;

    public AuthenticationErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
