package com.intuit.craftdemo.exception;

public class UserAlreadyLoggedInException extends BaseException {
    public UserAlreadyLoggedInException(String message, String errorCode) {
        super(message, errorCode);
    }
}
