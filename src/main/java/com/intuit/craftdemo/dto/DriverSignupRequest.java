package com.intuit.craftdemo.dto;

import com.intuit.craftdemo.entity.Address;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class DriverSignupRequest {
    @NotEmpty(message = "First name is required")
    private String firstName;
    
    private String lastName;
    
    @NotEmpty(message = "First name is required")
    private String email;
    
    @NotEmpty(message = "Password is required")
    private String password;
    
    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;
    
    @NotNull(message = "Address is required")
    private Address address;
}
