package com.intuit.craftdemo.service;


import com.intuit.craftdemo.entity.Admin;
import com.intuit.craftdemo.entity.AuthenticationToken;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.exception.AuthenticationFailedException;
import com.intuit.craftdemo.repository.AdminRepository;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.repository.TokenRepository;
import com.intuit.craftdemo.util.Helper;
import com.intuit.craftdemo.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private AdminRepository adminRepository;
    
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationToken saveToken(AuthenticationToken authenticationToken) {
        return tokenRepository.save(authenticationToken);
    }

    public AuthenticationToken getToken(String userEmail) {
        return tokenRepository.findTokenByUserEmail(userEmail);
    }

    public boolean invalidateToken(String token) {
        AuthenticationToken authenticationToken = tokenRepository.findTokenByToken(token);
        if (Helper.isNotNull(authenticationToken)) {
            tokenRepository.delete(authenticationToken);
            return true;
        }
        log.error("Authorization token invalid {}", token);
        return false;
    }

    public void authenticateDriverToken(String token) {
        checkTokenNotNull(token);
        if (!Helper.isNotNull(getDriverUsingToken(token))) {
            throw new AuthenticationFailedException(ResponseMessage.AUTH_TOKEN_IS_INVALID, 
                    ResponseMessage.ErrorCodes.AUTH_TOKEN_IS_INVALID.toString());
        }
    }

    public void authenticateAdminToken(String token) {
        checkTokenNotNull(token);
        if (!Helper.isNotNull(getAdminUsingToken(token))) {
            throw new AuthenticationFailedException(ResponseMessage.AUTH_TOKEN_IS_INVALID,
                    ResponseMessage.ErrorCodes.AUTH_TOKEN_IS_INVALID.toString());
        }
    }

    private static void checkTokenNotNull(String token) {
        if (!Helper.isNotNull(token)) {
            throw new AuthenticationFailedException(ResponseMessage.AUTH_TOKEN_IS_EMPTY,
                    ResponseMessage.ErrorCodes.AUTH_TOKEN_IS_EMPTY.toString());
        }
    }

    public Driver getDriverUsingToken(String token) {
        AuthenticationToken authenticationToken = tokenRepository.findTokenByToken(token);
        if (Helper.isNotNull(authenticationToken)) {
            if (Helper.isNotNull(authenticationToken.getUserEmail())) {
                return driverRepository.findByEmail(authenticationToken.getUserEmail());
            }
        }
        return null;
    }

    public Admin getAdminUsingToken(String token) {
        AuthenticationToken authenticationToken = tokenRepository.findTokenByToken(token);
        if (Helper.isNotNull(authenticationToken)) {
            if (Helper.isNotNull(authenticationToken.getUserEmail())) {
                return adminRepository.findByEmail(authenticationToken.getUserEmail());
            }
        }
        return null;
    }
}
