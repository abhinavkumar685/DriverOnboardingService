package com.intuit.craftdemo.dto;

public enum RequestParams {
    PHOTO("photo"),
    AADHAR_CARD("aadharCard"),
    PAN_CARD("panCard"),
    DRIVER_LICENSE("driverLicense"),
    TOKEN("token");

    private final String val;

    RequestParams(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return this.val;
    }
}
