package com.intuit.craftdemo.service;

import com.intuit.craftdemo.dto.DriverSignupRequest;
import com.intuit.craftdemo.dto.LoginRequest;
import com.intuit.craftdemo.dto.LoginResponse;
import com.intuit.craftdemo.dto.ProfileUpdateRequest;
import com.intuit.craftdemo.dto.ResponseDto;
import com.intuit.craftdemo.entity.AuthenticationToken;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.exception.DriverOnbaordingException;
import com.intuit.craftdemo.exception.InvalidUserPasswordException;
import com.intuit.craftdemo.exception.UserAlreadyExistException;
import com.intuit.craftdemo.exception.UserAlreadyLoggedInException;
import com.intuit.craftdemo.exception.UserNotFoundException;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.util.Helper;
import com.intuit.craftdemo.util.ResponseMessage;
import com.intuit.craftdemo.util.Role;
import com.intuit.craftdemo.util.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

import static com.intuit.craftdemo.util.ResponseMessage.USER_LOGGED_OUT;
import static com.intuit.craftdemo.util.ResponseMessage.USER_LOGOUT_ERROR;

@Service
public class DriverService implements UserService {
    
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private AuthenticationService authenticationService;
    
    private final Logger log = LoggerFactory.getLogger(DriverService.class);
    
    public Driver signUp(DriverSignupRequest driverSignupRequest) {
        validateUserAlreadyExist(driverSignupRequest);
        Driver driver  = Driver.builder().firstName(driverSignupRequest.getFirstName())
                .lastName(driverSignupRequest.getLastName())
                .email(driverSignupRequest.getEmail())
                .password(Helper.getEncryptedPassword(driverSignupRequest.getPassword()))
                .address(driverSignupRequest.getAddress())
                .phoneNumber(driverSignupRequest.getPhoneNumber())
                .role(Role.DRIVER)
                .userStatus(UserStatus.SIGNUP_DONE)
                .build();
        log.info("Driver signup successful for email {}", driver.getEmail());
        return driverRepository.save(driver);
    }
    
    public void updateDriverStatus(Driver driver, UserStatus status) {
        driver.setUserStatus(status);
        driverRepository.save(driver);
        log.info("Driver {} updated to status {} successfully", driver.getEmail(), status.toString());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Driver driver = getDriver(loginRequest.getEmail());
        if(!driver.getPassword().equals(Helper.getEncryptedPassword(loginRequest.getPassword()))) {
            throw new InvalidUserPasswordException(String.format(ResponseMessage.getINVALID_USER_PASSWORD(), loginRequest.getEmail()),
                    ResponseMessage.ErrorCodes.INVALID_USER_PASSWORD.toString());
        }

        AuthenticationToken token = authenticationService.getToken(driver.getEmail());
        if(Helper.isNotNull(token)) {
            throw new UserAlreadyLoggedInException(String.format(ResponseMessage.getUSER_ALREADY_LOGGED_IN(), loginRequest.getEmail()),
                    ResponseMessage.ErrorCodes.USER_ALREADY_LOGGED_IN.toString());
        }

        AuthenticationToken authenticationToken = AuthenticationToken.builder()
                .userEmail(driver.getEmail())
                .token(Helper.generateUserToken())
                .createdDate(new Date()).build();
        authenticationToken = authenticationService.saveToken(authenticationToken);
        log.info("Login successful for User: {}", driver.getEmail());
        return LoginResponse.builder().status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                .token(authenticationToken.getToken()).build();
    }

    public Driver getDriver(String email) {
        Driver driver = driverRepository.findByEmail(email);
        if(!Helper.isNotNull(driver)){
            throw new UserNotFoundException(String.format(ResponseMessage.getUSER_NOT_FOUND(), email),
                    ResponseMessage.ErrorCodes.USER_NOT_FOUND.toString());
        }
        return driver;
    }

    @Override
    public ResponseDto logout(String token) {
        boolean isTokenInvalidated = authenticationService.invalidateToken(token);
        if (isTokenInvalidated) {
            return ResponseDto.builder().status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                    .message(USER_LOGGED_OUT).build();
        } else {
            return ResponseDto.builder().status(ResponseMessage.ResponseCodes.ERROR.toString())
                    .message(USER_LOGOUT_ERROR).build();
        }
    }

    public void update(Driver driver, ProfileUpdateRequest profileUpdateRequest) {
        if(Helper.isNotBlank(profileUpdateRequest.getFirstName())) {
            driver.setFirstName(profileUpdateRequest.getFirstName());
        }
        if(Helper.isNotBlank(profileUpdateRequest.getLastName())) {
            driver.setLastName(profileUpdateRequest.getLastName());
        }
        if(Helper.isNotBlank(profileUpdateRequest.getDrivingLicenseNumber())) {
            driver.setDriverLicenseNumber(profileUpdateRequest.getDrivingLicenseNumber());
        }
        if(Helper.isNotNull(profileUpdateRequest.getAddress())) {
            driver.setAddress(profileUpdateRequest.getAddress());
        }
        if(Helper.isNotBlank(profileUpdateRequest.getGender())) {
            driver.setGender(profileUpdateRequest.getGender());
        }
        if(Helper.isNotBlank(profileUpdateRequest.getDateOfBirth())) {
            driver.setDateOfBirth(profileUpdateRequest.getDateOfBirth());
        }
        if(Helper.isNotBlank(profileUpdateRequest.getVehicleType())) {
            driver.setVehicleType(profileUpdateRequest.getVehicleType());
        }
        if(!Helper.isEmpty(profileUpdateRequest.getLanguagesSpoken())) {
            driver.setLanguagesSpoken(profileUpdateRequest.getLanguagesSpoken());
        }
        if(Helper.isNotBlank(profileUpdateRequest.getPhoneNumber())) {
            driver.setPhoneNumber(profileUpdateRequest.getPhoneNumber());
        }
        if(Helper.isNotBlank(profileUpdateRequest.getBankAccountNumber())) {
            driver.setBankAccountNumber(profileUpdateRequest.getBankAccountNumber());
        }
        if(Helper.isNotBlank(profileUpdateRequest.getNationality())) {
            driver.setNationality(profileUpdateRequest.getNationality());
        }
        driverRepository.save(driver);
    }
    
    public void markDriverAsReady(Driver driver) {
        if(driver.getUserStatus().equals(UserStatus.READY)) {
            throw new DriverOnbaordingException(String.format(ResponseMessage.getDRIVER_ALREADY_MARKED_AS_READY(),
                    driver.getEmail()), ResponseMessage.ErrorCodes.DRIVER_MARK_STATUS_READY_FAILED.toString());
        }
        if(!driver.getUserStatus().equals(UserStatus.ONBOARDING_COMPLETED)) {
            throw new DriverOnbaordingException(String.format(ResponseMessage.getDRIVER_CANNOT_BE_MARKED_READY(),
                    driver.getEmail()), ResponseMessage.ErrorCodes.DRIVER_MARK_STATUS_READY_FAILED.toString());
        }
        updateDriverStatus(driver, UserStatus.READY);
        log.info("Driver {} has been marked to Ready successfully", driver.getEmail());
    }

    private void validateUserAlreadyExist(DriverSignupRequest driverSignupRequest) {
        if(Helper.isNotNull(driverRepository.findByEmail(driverSignupRequest.getEmail()))) {
            throw new UserAlreadyExistException(String.format(ResponseMessage.getUSER_ALREADY_EXIST(),
                    driverSignupRequest.getEmail()),
                    ResponseMessage.ErrorCodes.USER_ALREADY_EXIST.toString());
        }
    }
}
