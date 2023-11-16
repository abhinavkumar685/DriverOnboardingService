package com.intuit.craftdemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {
    private String status;
    private String message;
}
