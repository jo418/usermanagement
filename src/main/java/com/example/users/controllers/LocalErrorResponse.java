package com.example.users.controllers;

public class LocalErrorResponse {
    private String error;

    public LocalErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
