package com.intuit.craftdemo.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
