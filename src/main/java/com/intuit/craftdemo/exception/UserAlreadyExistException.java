package com.intuit.craftdemo.exception;

public class UserAlreadyExistException extends BaseException {
    public UserAlreadyExistException(String message, String errorCode) {
        super(message, errorCode);
    }
}
