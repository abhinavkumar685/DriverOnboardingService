package com.intuit.craftdemo.resources;

import com.intuit.craftdemo.dto.DriverSignupRequest;
import com.intuit.craftdemo.dto.LoginRequest;
import com.intuit.craftdemo.dto.LoginResponse;
import com.intuit.craftdemo.dto.ProfileUpdateRequest;
import com.intuit.craftdemo.dto.ResponseDto;
import com.intuit.craftdemo.entity.Address;
import com.intuit.craftdemo.entity.AuthenticationToken;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.util.ResponseMessage;
import com.intuit.craftdemo.util.Role;

import static com.intuit.craftdemo.util.ResponseMessage.USER_LOGGED_OUT;
import static com.intuit.craftdemo.util.ResponseMessage.USER_LOGOUT_ERROR;

public class TestResource {
    private static final Driver TEST_DRIVER = Driver.builder().firstName("First Name")
            .lastName("Last Name")
            .email("test@email.com")
            .password("Password@123")
            .address(Address.builder().address1("Test Address")
                    .city("Bangalore")
                    .state("Karnataka")
                    .zipcode("5610102").build())
            .phoneNumber("9999000099")
            .role(Role.DRIVER)
            .dateOfBirth("01-01-1994")
            .vehicleType("car")
            .driverLicenseNumber("DL123456")
            .bankAccountNumber("SBI0012345678")
            .gender("male")
            .build();
    
    public static Driver getDriver() {
        return TEST_DRIVER;
    }
    
    public static ResponseDto getSuccessLogoutResponse() {
        return ResponseDto.builder().status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                .message(USER_LOGGED_OUT).build();
    }

    public static ResponseDto getFailedLogoutResponse() {
        return ResponseDto.builder().status(ResponseMessage.ResponseCodes.ERROR.toString())
                .message(USER_LOGOUT_ERROR).build();
    }
    
    public static LoginRequest getLoginRequest() {
        return LoginRequest.builder().email(TEST_DRIVER.getEmail()).password(TEST_DRIVER.getPassword()).build();
    }

    public static LoginResponse getLoginResponse() {
        return LoginResponse.builder().status("success").token("random-auth-login-token").build();
    }
    
    public static AuthenticationToken getAuthenticationToken() {
        return AuthenticationToken.builder().userEmail(TEST_DRIVER.getEmail()).token("random-auth-login-token").build();
    }
    
    public static DriverSignupRequest getDriverSignupRequest() {
        return DriverSignupRequest.builder()
                .firstName(TEST_DRIVER.getFirstName())
                .lastName(TEST_DRIVER.getLastName())
                .password(TEST_DRIVER.getPassword())
                .address(TEST_DRIVER.getAddress())
                .phoneNumber(TEST_DRIVER.getPhoneNumber())
                .email(TEST_DRIVER.getEmail())
                .build();
    }

    public static ProfileUpdateRequest getDriverProfileUpdateRequest() {
        return ProfileUpdateRequest.builder()
                .firstName(TEST_DRIVER.getFirstName())
                .lastName(TEST_DRIVER.getLastName())
                .address(TEST_DRIVER.getAddress())
                .phoneNumber(TEST_DRIVER.getPhoneNumber())
                .dateOfBirth(TEST_DRIVER.getDateOfBirth())
                .drivingLicenseNumber(TEST_DRIVER.getDriverLicenseNumber())
                .vehicleType(TEST_DRIVER.getVehicleType())
                .bankAccountNumber(TEST_DRIVER.getBankAccountNumber())
                .gender(TEST_DRIVER.getGender())
                .build();
    }
}
