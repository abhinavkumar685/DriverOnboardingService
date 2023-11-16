package com.intuit.craftdemo.controller;

import com.intuit.craftdemo.dto.DriverSignupRequest;
import com.intuit.craftdemo.dto.LoginRequest;
import com.intuit.craftdemo.dto.LoginResponse;
import com.intuit.craftdemo.dto.ProfileUpdateRequest;
import com.intuit.craftdemo.dto.RequestParams;
import com.intuit.craftdemo.dto.ResponseDto;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.service.AuthenticationService;
import com.intuit.craftdemo.service.DriverService;
import com.intuit.craftdemo.service.OnboardingService;
import com.intuit.craftdemo.util.ResponseMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static com.intuit.craftdemo.util.ResponseMessage.DRIVER_MARKED_TO_READY;
import static com.intuit.craftdemo.util.ResponseMessage.DRIVER_ONBOARDING_STARTED;
import static com.intuit.craftdemo.util.ResponseMessage.USER_UPDATED;


@RestController
@RequestMapping("driver/v1")
public class DriverController {
    private final Logger log = LoggerFactory.getLogger(DriverController.class);
    
    @Autowired
    private DriverService driverService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private OnboardingService onboardingService;
    
    @GetMapping("/fetch/info")
    public ResponseEntity<Driver> getInfo(@RequestParam(value = "token") String token) {
        authenticationService.authenticateDriverToken(token);
        return ResponseEntity.ok(authenticationService.getDriverUsingToken(token));
    }
    
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signUp(@Validated @RequestBody DriverSignupRequest driverSignupRequest) {
        log.info("Received request for Driver Signup: {}", driverSignupRequest);
        driverService.signUp(driverSignupRequest);
        return ResponseEntity.ok().body(ResponseDto.builder()
                .message(ResponseMessage.getUSER_CREATED())
                .status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request received for user email: {}", loginRequest.getEmail());
        LoginResponse loginResponse = driverService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestParam(value = "token") String token) {
        log.info("Logout request received for the user");
        ResponseDto responseDto = driverService.logout(token);
        if(responseDto.getStatus().equals(ResponseMessage.ResponseCodes.SUCCESS.toString())) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateProfile(@RequestParam(value = "token") String token,
                                           @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        log.info("Profile update request received {}", profileUpdateRequest);
        authenticationService.authenticateDriverToken(token);
        driverService.update(authenticationService.getDriverUsingToken(token), profileUpdateRequest);
        return ResponseEntity.ok(ResponseDto.builder().status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                .message(USER_UPDATED).build());
    }
    
    @PostMapping("/onboard")
    public ResponseEntity<ResponseDto> onboardDriver(@RequestParam(value = "token") String token,
                                           @RequestParam("photo") MultipartFile photo,
                                           @RequestParam("aadharCard") MultipartFile aadharCard,
                                           @RequestParam("panCard") MultipartFile panCard,
                                           @RequestParam("drivingLicense") MultipartFile drivingLicense) {
        log.info("Driver onboarding request received");
        authenticationService.authenticateDriverToken(token);
        Map<RequestParams, MultipartFile> filesMap = new HashMap<>();
        filesMap.put(RequestParams.PHOTO, photo);
        filesMap.put(RequestParams.AADHAR_CARD, aadharCard);
        filesMap.put(RequestParams.PAN_CARD, panCard);
        filesMap.put(RequestParams.DRIVER_LICENSE, drivingLicense);
        onboardingService.onboardDriver(authenticationService.getDriverUsingToken(token), filesMap);
        return ResponseEntity.ok(ResponseDto.builder().status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                .message(DRIVER_ONBOARDING_STARTED).build());
    }
    
    @PutMapping("/markReady")
    public ResponseEntity<ResponseDto> markDriverReady(@RequestParam(value = "token") String token) {
        log.info("Request received to mark driver as Ready");
        authenticationService.authenticateDriverToken(token);
        driverService.markDriverAsReady(authenticationService.getDriverUsingToken(token));
        return ResponseEntity.ok(ResponseDto.builder().status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                .message(DRIVER_MARKED_TO_READY).build());
    }
}
