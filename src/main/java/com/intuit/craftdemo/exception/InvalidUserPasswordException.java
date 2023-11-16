package com.intuit.craftdemo.exception;

public class InvalidUserPasswordException extends BaseException {
    public InvalidUserPasswordException(String message, String errorCode) {
        super(message, errorCode);
    }
}
