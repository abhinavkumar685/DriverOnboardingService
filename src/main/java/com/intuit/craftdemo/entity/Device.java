package com.intuit.craftdemo.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "device")
public class Device {
    private String deviceId;
    private String serialNumber;
    private Boolean isShipped;
    private String driverId;
}
