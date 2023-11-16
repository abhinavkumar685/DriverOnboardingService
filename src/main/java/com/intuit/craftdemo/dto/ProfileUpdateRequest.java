package com.intuit.craftdemo.dto;

import com.intuit.craftdemo.entity.Address;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String drivingLicenseNumber;
    private String bankAccountNumber;
    private String dateOfBirth;
    private String gender;
    private String nationality;
    private Address address;
    private String vehicleType;
    private List<String> languagesSpoken;
}
