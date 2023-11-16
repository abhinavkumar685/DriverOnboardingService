package com.intuit.craftdemo.service;

import com.intuit.craftdemo.dto.LoginRequest;
import com.intuit.craftdemo.dto.ResponseDto;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.exception.DriverOnbaordingException;
import com.intuit.craftdemo.exception.InvalidUserPasswordException;
import com.intuit.craftdemo.exception.UserAlreadyExistException;
import com.intuit.craftdemo.exception.UserAlreadyLoggedInException;
import com.intuit.craftdemo.exception.UserNotFoundException;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.resources.TestResource;
import com.intuit.craftdemo.util.ResponseMessage;
import com.intuit.craftdemo.util.Role;
import com.intuit.craftdemo.util.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class DriverServiceTest {
    @InjectMocks
    private DriverService driverService;
    @Mock
    private DriverRepository driverRepository;
    @Mock
    private AuthenticationService authenticationService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUpFailed_EmailAlreadyExist() {
        when(driverRepository.findByEmail(any())).thenReturn(TestResource.getDriver());
        UserAlreadyExistException userAlreadyExistException = assertThrows(UserAlreadyExistException.class, 
                () -> driverService.signUp(TestResource.getDriverSignupRequest()));
        assertEquals(String.format(ResponseMessage.getUSER_ALREADY_EXIST(),
                TestResource.getDriverSignupRequest().getEmail()), userAlreadyExistException.getMessage());
        assertEquals(ResponseMessage.ErrorCodes.USER_ALREADY_EXIST.toString(), userAlreadyExistException.getErrorCode());
        verify(driverRepository, times(1)).findByEmail(TestResource.getDriver().getEmail());
    }

    @Test
    void testSignUpSuccessful() {
        when(driverRepository.findByEmail(any())).thenReturn(null);
        when(driverRepository.save(any())).thenReturn(TestResource.getDriver());
        Driver result = driverService.signUp(TestResource.getDriverSignupRequest());
        assertEquals(TestResource.getDriver().getEmail(), result.getEmail());
        assertEquals(TestResource.getDriver().getRole(), Role.DRIVER);
        verify(driverRepository, times(1)).save(any());
    }
    
    @Test
    void testLoginFailed_UserNotFound() {
        when(driverRepository.findByEmail(any())).thenReturn(null);
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> driverService.login(TestResource.getLoginRequest()));
        assertEquals(String.format(ResponseMessage.getUSER_NOT_FOUND(),
                TestResource.getDriverSignupRequest().getEmail()), userNotFoundException.getMessage());
        assertEquals(ResponseMessage.ErrorCodes.USER_NOT_FOUND.toString(), userNotFoundException.getErrorCode());
        verify(driverRepository, times(1)).findByEmail(any());
    }

    @Test
    void testLoginFailed_InvalidUserPassword() {
        when(driverRepository.findByEmail(any())).thenReturn(TestResource.getDriver());
        LoginRequest loginRequest = LoginRequest.builder().email(TestResource.getDriver().getEmail()).password("Invalid Password").build();
        InvalidUserPasswordException invalidUserPasswordException = assertThrows(InvalidUserPasswordException.class,
                () -> driverService.login(loginRequest));
        assertEquals(String.format(ResponseMessage.getINVALID_USER_PASSWORD(),
                TestResource.getDriverSignupRequest().getEmail()), invalidUserPasswordException.getMessage());
        assertEquals(ResponseMessage.ErrorCodes.INVALID_USER_PASSWORD.toString(), invalidUserPasswordException.getErrorCode());
        verify(driverRepository, times(1)).findByEmail(any());
    }
    
    @Test
    void testLogout() {
        when(authenticationService.invalidateToken(any())).thenReturn(true);
        ResponseDto responseDto = driverService.logout("auth-token");
        assertEquals(responseDto.getStatus(), "success");
    }
    
    @Test
    void testMarkDriverReadyFailed_OnboardingNotCompleted() {
        Driver driver = Driver.builder().email(TestResource.getDriver().getEmail()).userStatus(UserStatus.BLOCKED).build();
        DriverOnbaordingException driverOnbaordingException = assertThrows(DriverOnbaordingException.class,
                () -> driverService.markDriverAsReady(driver));
        assertEquals(String.format(ResponseMessage.getDRIVER_CANNOT_BE_MARKED_READY(),
                TestResource.getDriverSignupRequest().getEmail()), driverOnbaordingException.getMessage());
        assertEquals(ResponseMessage.ErrorCodes.DRIVER_MARK_STATUS_READY_FAILED.toString(), driverOnbaordingException.getErrorCode());
    }

    @Test
    void testMarkDriverReadyFailed_DriverAlreadyMarkedReady() {
        Driver driver = Driver.builder().email(TestResource.getDriver().getEmail()).userStatus(UserStatus.READY).build();
        DriverOnbaordingException driverOnbaordingException = assertThrows(DriverOnbaordingException.class,
                () -> driverService.markDriverAsReady(driver));
        assertEquals(String.format(ResponseMessage.getDRIVER_ALREADY_MARKED_AS_READY(),
                TestResource.getDriverSignupRequest().getEmail()), driverOnbaordingException.getMessage());
        assertEquals(ResponseMessage.ErrorCodes.DRIVER_MARK_STATUS_READY_FAILED.toString(), driverOnbaordingException.getErrorCode());
    }

    @Test
    void testMarkDriverReadySuccessful() {
        Driver driver = Driver.builder().email(TestResource.getDriver().getEmail()).userStatus(UserStatus.ONBOARDING_COMPLETED).build();
        driverService.markDriverAsReady(driver);
        assertEquals(driver.getUserStatus(), UserStatus.READY);
    }
}