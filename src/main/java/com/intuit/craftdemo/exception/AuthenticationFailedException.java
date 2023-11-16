package com.intuit.craftdemo.exception;

public class AuthenticationFailedException extends BaseException {
    public AuthenticationFailedException(String message, String errorCode) {
        super(message, errorCode);
    }
}
