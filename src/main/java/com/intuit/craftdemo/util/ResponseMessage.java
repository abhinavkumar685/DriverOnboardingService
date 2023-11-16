package com.intuit.craftdemo.util;

import lombok.Getter;

public class ResponseMessage {

    public static final String AUTH_TOKEN_IS_EMPTY = "Authentication Token is not present";
    public static final String AUTH_TOKEN_IS_INVALID= "Authentication Token is Invalid";
    @Getter
    public static final String USER_CREATED = "User Created Successfully";
    @Getter
    public static final String USER_UPDATED = "User Updated Successfully";
    @Getter
    public static final String USER_LOGGED_OUT = "User Logged Out Successfully";
    @Getter
    public static final String USER_LOGOUT_ERROR = "User Logout Error Invalid Token";
    @Getter
    public static final String USER_LOGGED_FAILED = "User Logged Out Failed";
    @Getter
    public static final String USER_ALREADY_EXIST = "User %s already exist";
    @Getter
    public static final String USER_NOT_FOUND = "User %s not found";
    @Getter
    public static final String INVALID_USER_PASSWORD = "Invalid password provided for User %s. Please check password and retry";
    @Getter
    public static final String USER_ALREADY_LOGGED_IN = "User %s already logged in";
    @Getter
    public static final String DRIVER_ONBOARDING_STARTED = "Driver Onboarding Started";
    @Getter
    public static final String DRIVER_MARKED_TO_READY = "Driver successfully marked to Ready";
    @Getter
    public static final String DRIVER_DOCUMENT_SUBMISSION_FAILED = "Driver %s document submission failed";
    @Getter
    public static final String DRIVER_BACKGROUND_VERIFICATION_FAILED = "Driver %s background verification failed";
    @Getter
    public static final String DRIVER_CANNOT_BE_MARKED_READY = "Driver %s cannot be marked ready since onboarding is not complete";
    @Getter
    public static final String DRIVER_ALREADY_MARKED_AS_READY = "Driver %s already marked as ready";
    @Getter
    public static final String DRIVER_STATUS_UPDATED = "Driver status updated Successfully";
    
    
    public enum ResponseCodes {
        SUCCESS("success"),
        ERROR("error");

        private final String val;
        
        ResponseCodes(String val) { 
            this.val = val;
        }

        @Override
        public String toString() {
            return this.val;
        }
    }
    
    public enum ErrorCodes {
        GENERIC_ERROR("ERR-001"),
        USER_ALREADY_EXIST("ERR-002"),
        USER_NOT_FOUND("ERR-003"),
        INVALID_USER_PASSWORD("ERR-004"),
        USER_ALREADY_LOGGED_IN("ERR-005"),
        AUTH_TOKEN_IS_EMPTY("ERR-006"),
        AUTH_TOKEN_IS_INVALID("ERR-007"),
        DOCUMENT_SUBMISSION_FAILED("ERR-008"),
        DOCUMENT_VERIFICATION_FAILED("ERR-009"),
        
        DRIVER_BACKGROUND_VERIFICATION_FAILED("ERROR-010"),
        DRIVER_TRACKING_DEVICE_SHIPMENT_FAILED("ERROR-011"),
        DRIVER_MARK_STATUS_READY_FAILED("ERROR-011");

        private final String val;

        ErrorCodes(String val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return this.val;
        }
    }
}
