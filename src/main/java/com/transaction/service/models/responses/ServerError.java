package com.transaction.service.models.responses;

public class ServerError extends Exception {
    
    private String message;

    private String code;

    public ServerError(String message, String code) {
        setMessage(message);
        setCode(code);
    }

    public ServerError(String code) {
        this.message = "Server encountered an unexpected error when processing the request and could not complete the request";
        setCode(code);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        if (message.trim().length() < 0) {
            throw new IllegalArgumentException("Message should have length greater than 0");
        }

        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        if (message.trim().length() < 0) {
            throw new IllegalArgumentException("Code should have length greater than 0");
        }

        this.code = code;
    }
    
}
