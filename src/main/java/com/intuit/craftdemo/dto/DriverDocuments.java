package com.intuit.craftdemo.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@Builder
public class DriverDocuments {
    String email;
    Resource photo;
    Resource aadharCard;
    Resource panCard;
    Resource drivingLicense;
}
