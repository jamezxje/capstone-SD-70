package org.fpoly.capstone.exceptions;

public class ErrorResponse {
    private String message;
    private String errorCode;

    // Constructor, getters, setters
    public ErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

