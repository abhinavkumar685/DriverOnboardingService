package com.intuit.craftdemo.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "driver_document")
public class DriverDocument {
    @Id
    private String id;
    private String driverEmail;
    private Binary photo;
    private Binary aadharCard;
    private Binary panCard;
    private Binary driverLicense;
}
