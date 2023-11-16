package com.intuit.craftdemo.service;

import com.intuit.craftdemo.dto.AdminSignupRequest;
import com.intuit.craftdemo.dto.DriverDocuments;
import com.intuit.craftdemo.dto.LoginRequest;
import com.intuit.craftdemo.dto.LoginResponse;
import com.intuit.craftdemo.dto.ResponseDto;
import com.intuit.craftdemo.entity.Admin;
import com.intuit.craftdemo.entity.AuthenticationToken;
import com.intuit.craftdemo.entity.DriverDocument;
import com.intuit.craftdemo.exception.InvalidUserPasswordException;
import com.intuit.craftdemo.exception.UserAlreadyExistException;
import com.intuit.craftdemo.exception.UserAlreadyLoggedInException;
import com.intuit.craftdemo.exception.UserNotFoundException;
import com.intuit.craftdemo.repository.AdminRepository;
import com.intuit.craftdemo.repository.DriverDocumentRepository;
import com.intuit.craftdemo.util.Helper;
import com.intuit.craftdemo.util.ResponseMessage;
import com.intuit.craftdemo.util.Role;
import com.intuit.craftdemo.util.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.intuit.craftdemo.util.ResponseMessage.USER_LOGGED_OUT;
import static com.intuit.craftdemo.util.ResponseMessage.USER_LOGOUT_ERROR;

@Service
public class AdminService implements UserService {
    private final Logger log = LoggerFactory.getLogger(AdminService.class);
    
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DriverDocumentRepository driverDocumentRepository;
    @Autowired
    private DriverService driverService;
    
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Admin admin = adminRepository.findByEmail(loginRequest.getEmail());
        if(!Helper.isNotNull(admin)){
            throw new UserNotFoundException(String.format(ResponseMessage.getUSER_NOT_FOUND(), loginRequest.getEmail()),
                    ResponseMessage.ErrorCodes.USER_NOT_FOUND.toString());
        }

        if(!admin.getPassword().equals(Helper.getEncryptedPassword(loginRequest.getPassword()))) {
            throw new InvalidUserPasswordException(String.format(ResponseMessage.getINVALID_USER_PASSWORD(), loginRequest.getEmail()),
                    ResponseMessage.ErrorCodes.INVALID_USER_PASSWORD.toString());
        }

        AuthenticationToken token = authenticationService.getToken(admin.getEmail());
        if(Helper.isNotNull(token)) {
            throw new UserAlreadyLoggedInException(String.format(ResponseMessage.getUSER_ALREADY_LOGGED_IN(), loginRequest.getEmail()),
                    ResponseMessage.ErrorCodes.USER_ALREADY_LOGGED_IN.toString());
        }

        AuthenticationToken authenticationToken = AuthenticationToken.builder()
                .userEmail(admin.getEmail())
                .token(Helper.generateUserToken())
                .createdDate(new Date()).build();
        authenticationToken = authenticationService.saveToken(authenticationToken);
        log.info("Login successful for User: {}", admin.getEmail());
        return LoginResponse.builder().status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                .token(authenticationToken.getToken()).build();
    }

    @Override
    public ResponseDto logout(String token) {
        if (authenticationService.invalidateToken(token)) {
            return ResponseDto.builder().status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                    .message(USER_LOGGED_OUT).build();
        } else {
            return ResponseDto.builder().status(ResponseMessage.ResponseCodes.ERROR.toString())
                    .message(USER_LOGOUT_ERROR).build();
        }
    }

    public Admin signUp(AdminSignupRequest adminSignupRequest) {
        validateUserAlreadyExist(adminSignupRequest);
        Admin admin  = Admin.builder().firstName(adminSignupRequest.getFirstName())
                .lastName(adminSignupRequest.getLastName())
                .email(adminSignupRequest.getEmail())
                .password(Helper.getEncryptedPassword(adminSignupRequest.getPassword()))
                .phoneNumber(adminSignupRequest.getPhoneNumber())
                .role(Role.ADMIN)
                .build();
        log.info("Driver signup successful for email {}", admin.getEmail());
        return adminRepository.save(admin);
    }

    private void validateUserAlreadyExist(AdminSignupRequest adminSignupRequest) {
        if(Helper.isNotNull(adminRepository.findByEmail(adminSignupRequest.getEmail()))) {
            throw new UserAlreadyExistException(String.format(ResponseMessage.getUSER_ALREADY_EXIST(),
                    adminSignupRequest.getEmail()),
                    ResponseMessage.ErrorCodes.USER_ALREADY_EXIST.toString());
        }
    }

    public DriverDocuments fetchDriverDocuments(String driverEmail) {
        driverService.getDriver(driverEmail);
        DriverDocument driverDocument = driverDocumentRepository.findByDriverEmail(driverEmail);
        return DriverDocuments.builder()
                .email(driverEmail)
                .photo(new ByteArrayResource(driverDocument.getPhoto().getData()))
                .aadharCard(new ByteArrayResource(driverDocument.getAadharCard().getData()))
                .panCard(new ByteArrayResource(driverDocument.getPanCard().getData()))
                .drivingLicense(new ByteArrayResource(driverDocument.getDriverLicense().getData()))
                .build();
    }

    public void updateDriverStatus(String driverEmail, UserStatus driverStatus) {
        driverService.updateDriverStatus(driverService.getDriver(driverEmail), driverStatus);
    }
}
