package com.intuit.craftdemo.service;

import com.intuit.craftdemo.dto.LoginRequest;
import com.intuit.craftdemo.dto.LoginResponse;
import com.intuit.craftdemo.dto.ResponseDto;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
    ResponseDto logout(String token);
}
