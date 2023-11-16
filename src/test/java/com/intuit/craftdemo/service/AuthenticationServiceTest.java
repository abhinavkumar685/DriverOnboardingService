package com.intuit.craftdemo.service;

import com.intuit.craftdemo.entity.AuthenticationToken;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.exception.AuthenticationFailedException;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.repository.TokenRepository;
import com.intuit.craftdemo.resources.TestResource;
import com.intuit.craftdemo.util.ResponseMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private DriverRepository driverRepository;

    @Test
    public void testSaveToken() {
        AuthenticationToken token = TestResource.getAuthenticationToken();
        authenticationService.saveToken(token);
        verify(tokenRepository).save(token);
    }

    @Test
    public void testGetToken() {
        when(tokenRepository.findTokenByUserEmail(any())).thenReturn(TestResource.getAuthenticationToken());
        AuthenticationToken authenticationToken = 
                authenticationService.getToken(TestResource.getAuthenticationToken().getUserEmail());
        assertNotNull(authenticationToken);
        verify(tokenRepository).findTokenByUserEmail(TestResource.getAuthenticationToken().getUserEmail());
    }
    
    @Test
    public void testInvalidateTokenSuccessful() {
        when(tokenRepository.findTokenByToken(any())).thenReturn(TestResource.getAuthenticationToken());
        boolean result = authenticationService.invalidateToken(TestResource.getAuthenticationToken().getToken());
        assertTrue(result);
        verify(tokenRepository).delete(TestResource.getAuthenticationToken());
    }

    @Test
    public void testInvalidateTokenFailed() {
        when(tokenRepository.findTokenByToken(any())).thenReturn(null);
        boolean result = authenticationService.invalidateToken(TestResource.getAuthenticationToken().getToken());
        assertFalse(result);
    }

    @Test
    public void testAuthenticateTokenFailed_EmptyTokenIsNull() {
        AuthenticationFailedException authenticationFailedException = assertThrows(AuthenticationFailedException.class,
                () -> authenticationService.authenticateDriverToken(null));
        assertEquals(ResponseMessage.AUTH_TOKEN_IS_EMPTY, authenticationFailedException.getMessage());
        assertEquals(ResponseMessage.ErrorCodes.AUTH_TOKEN_IS_EMPTY.toString(), authenticationFailedException.getErrorCode());
    }

    @Test
    public void testAuthenticateTokenFailed_InvalidToken() {
        when(tokenRepository.findTokenByToken(any())).thenReturn(TestResource.getAuthenticationToken());
        when(driverRepository.findByEmail(any())).thenReturn(null);
        AuthenticationFailedException authenticationFailedException = assertThrows(AuthenticationFailedException.class,
                () -> authenticationService.authenticateDriverToken(TestResource.getAuthenticationToken().getToken()));
        assertEquals(ResponseMessage.AUTH_TOKEN_IS_INVALID, authenticationFailedException.getMessage());
        assertEquals(ResponseMessage.ErrorCodes.AUTH_TOKEN_IS_INVALID.toString(), authenticationFailedException.getErrorCode());
    }

    @Test
    public void testAuthenticateTokenFailed_ValidTokenWithDriverNotFound() {
        when(tokenRepository.findTokenByToken(any())).thenReturn(null);
        AuthenticationFailedException authenticationFailedException = assertThrows(AuthenticationFailedException.class,
                () -> authenticationService.authenticateDriverToken(TestResource.getAuthenticationToken().getToken()));
        assertEquals(ResponseMessage.AUTH_TOKEN_IS_INVALID, authenticationFailedException.getMessage());
        assertEquals(ResponseMessage.ErrorCodes.AUTH_TOKEN_IS_INVALID.toString(), authenticationFailedException.getErrorCode());
    }
    
    @Test
    public void testGetDriverUsingToken() {
        when(tokenRepository.findTokenByToken(any())).thenReturn(TestResource.getAuthenticationToken());
        when(driverRepository.findByEmail(any())).thenReturn(TestResource.getDriver());
        Driver result = authenticationService.getDriverUsingToken(TestResource.getAuthenticationToken().getToken());
        assertEquals(result, TestResource.getDriver());
    }
}