package com.intuit.craftdemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private final String error_code;
    private final String error_message;
}
