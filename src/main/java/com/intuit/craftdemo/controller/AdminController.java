package com.intuit.craftdemo.controller;

import com.intuit.craftdemo.dto.AdminSignupRequest;
import com.intuit.craftdemo.dto.DriverDocuments;
import com.intuit.craftdemo.dto.LoginRequest;
import com.intuit.craftdemo.dto.LoginResponse;
import com.intuit.craftdemo.dto.ResponseDto;
import com.intuit.craftdemo.entity.Admin;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.service.AdminService;
import com.intuit.craftdemo.service.AuthenticationService;
import com.intuit.craftdemo.service.DriverService;
import com.intuit.craftdemo.util.ResponseMessage;
import com.intuit.craftdemo.util.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/v1")
public class AdminController {
    private final Logger log = LoggerFactory.getLogger(AdminController.class);
    
    @Autowired
    private AdminService adminService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/fetch/info")
    public ResponseEntity<Admin> fetchAdminInfo(@RequestParam(value = "token") String token)  {
        authenticationService.authenticateAdminToken(token);
        return ResponseEntity.ok(authenticationService.getAdminUsingToken(token));
    }

    @GetMapping("/fetch/driverInfo")
    public ResponseEntity<Driver> fetchDriverInfo(@RequestParam(value = "token") String token,
                                                 @RequestParam(value = "driverEmail") String driverEmail)  {
        authenticationService.authenticateAdminToken(token);
        return ResponseEntity.ok(driverService.getDriver(driverEmail));
    }

    @GetMapping("/fetch/drivers")
    public ResponseEntity<Page<Driver>> getAllDrivers(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        authenticationService.authenticateAdminToken(token);
        log.info("Request recieved to fetch driver list for page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Driver> driversPage = driverRepository.findAll(pageable);
        return ResponseEntity.ok(driversPage);
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signUp(@Validated @RequestBody AdminSignupRequest adminSignupRequest) {
        log.info("Received request for Driver Signup: {}", adminSignupRequest);
        adminService.signUp(adminSignupRequest);
        return ResponseEntity.ok().body(ResponseDto.builder()
                .message(ResponseMessage.getUSER_CREATED())
                .status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request received for user email: {}", loginRequest.getEmail());
        LoginResponse loginResponse = adminService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestParam(value = "token") String token) {
        log.info("Logout request received for the user");
        ResponseDto responseDto = adminService.logout(token);
        if(responseDto.getStatus().equals(ResponseMessage.ResponseCodes.SUCCESS.toString())) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.badRequest().body(responseDto);
        }
    }
    
    @GetMapping("/fetch/driverPhoto")
    public ResponseEntity<Resource> getDriverDocuments(@RequestParam(value = "token") String token,
                                                       @RequestParam(value = "driverEmail") String driverEmail) {
        authenticationService.authenticateAdminToken(token);
        log.info("Request received to fetch document of driver {}", driverEmail);
        DriverDocuments driverDocuments = adminService.fetchDriverDocuments(driverEmail);
        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "driver_photo.png" + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(driverDocuments.getPhoto());
    }
    
    @PutMapping("/update/driverStatus")
    public ResponseEntity<ResponseDto> updateDriverStatus(@RequestParam(value = "token") String token,
                                                          @RequestParam(value = "driverEmail") String driverEmail,
                                                          @RequestParam(value = "status") UserStatus driverStatus) {
        authenticationService.authenticateAdminToken(token);
        log.info("Request received to update driver {} status to {}", driverEmail, driverStatus);
        adminService.updateDriverStatus(driverEmail, driverStatus);
        return ResponseEntity.ok().body(ResponseDto.builder()
                .message(ResponseMessage.getDRIVER_STATUS_UPDATED())
                .status(ResponseMessage.ResponseCodes.SUCCESS.toString())
                .build());
    }
}
