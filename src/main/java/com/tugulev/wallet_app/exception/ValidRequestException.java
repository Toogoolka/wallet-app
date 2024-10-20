package com.tugulev.wallet_app.exception;

public class ValidRequestException extends RuntimeException {
    public ValidRequestException() {
    }

    public ValidRequestException(String message) {
        super(message);
    }
}
