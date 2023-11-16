package com.intuit.craftdemo.controller;

import com.intuit.craftdemo.dto.LoginResponse;
import com.intuit.craftdemo.dto.ResponseDto;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.resources.TestResource;
import com.intuit.craftdemo.service.AuthenticationService;
import com.intuit.craftdemo.service.DriverService;
import com.intuit.craftdemo.service.OnboardingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DriverControllerTest {
    @InjectMocks
    private DriverController driverController;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private DriverService driverService;
    @Mock
    private OnboardingService onboardingService;

    
    @Test
    public void testFetchDriverInfo_ValidToken() {
        String token = "valid_token";
        Driver driver = TestResource.getDriver();
        doNothing().when(authenticationService).authenticateDriverToken(token);
        when(authenticationService.getDriverUsingToken(token)).thenReturn(TestResource.getDriver());
        Driver result = driverController.getInfo(token).getBody();
        assertEquals(driver, result);
    }

    @Test
    public void testLogin_ValidToken_SuccessfulLogin() {
        when(driverService.login(any())).thenReturn(TestResource.getLoginResponse());
        ResponseEntity<LoginResponse> response = driverController.login(TestResource.getLoginRequest());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(TestResource.getAuthenticationToken().getToken(), response.getBody().getToken());
    }

    @Test
    public void testLogout_ValidToken_SuccessfulLogout() {
        String token = "valid_token";
        when(driverService.logout(token)).thenReturn(TestResource.getSuccessLogoutResponse());
        ResponseEntity<ResponseDto> response = driverController.logout(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
    }

    @Test
    public void testLogout_InvalidToken_FailedLogout() {
        String token = "invalid_token";
        when(driverService.logout(token)).thenReturn(TestResource.getFailedLogoutResponse());
        ResponseEntity<ResponseDto> response = driverController.logout(token);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
    }
    
    @Test
    public void testSignup() {
        when(driverService.signUp(any())).thenReturn(TestResource.getDriver());
        ResponseEntity<ResponseDto> response = driverController.signUp(TestResource.getDriverSignupRequest());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
    }

    @Test
    public void testUpdateProfile() {
        String token = "valid_token";
        doNothing().when(authenticationService).authenticateDriverToken(token);
        ResponseEntity<ResponseDto> response = driverController.updateProfile(token, 
                TestResource.getDriverProfileUpdateRequest());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
    }
    
    @Test
    public void testOnboardDriver() {
        String token = "valid_token";
        doNothing().when(authenticationService).authenticateDriverToken(token);
        MultipartFile photo = new MockMultipartFile("photo.jpg", "photo.jpg", null, new byte[0]);
        MultipartFile aadharCard = new MockMultipartFile("aadharCard.jpg", "aadharCard.jpg", null, new byte[0]);
        MultipartFile drivringLicense = new MockMultipartFile("drivringLicense.jpg", "drivringLicense.jpg", null, new byte[0]);
        MultipartFile panCard = new MockMultipartFile("panCard.jpg", "panCard.jpg", null, new byte[0]);
        ResponseEntity<ResponseDto> response = driverController.onboardDriver(token, photo, aadharCard, panCard, drivringLicense);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Driver Onboarding Started", response.getBody().getMessage());
    }
    
    @Test
    public void testMarkDriverReady() {
        String token = "valid_token";
        doNothing().when(authenticationService).authenticateDriverToken(token);
        ResponseEntity<ResponseDto> response = driverController.markDriverReady(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
    }
}
