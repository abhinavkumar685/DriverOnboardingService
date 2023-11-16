package com.intuit.craftdemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String status;
    private String token;
}
