package com.intuit.craftdemo.entity;

import com.intuit.craftdemo.util.Role;
import com.intuit.craftdemo.util.UserStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "drivers")
public class Driver implements User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String password;
    private String phoneNumber;
    private String dateOfBirth;
    private String gender;
    private String nationality;
    private Address address;
    private String driverLicenseNumber;
    private String bankAccountNumber;
    private String vehicleType;
    private List<String> languagesSpoken;
    private UserStatus userStatus;
}
