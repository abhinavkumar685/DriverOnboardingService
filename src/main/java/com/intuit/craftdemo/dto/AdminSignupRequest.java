package com.intuit.craftdemo.dto;

import com.intuit.craftdemo.entity.Address;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AdminSignupRequest {
    @NotEmpty @NotNull(message = "First name is required")
    private String firstName;

    private String lastName;

    @NotEmpty @NotNull(message = "First name is required")
    private String email;

    @NotEmpty @NotNull(message = "Password is required")
    private String password;

    @NotEmpty @NotNull(message = "Phone number is required")
    private String phoneNumber;
}
