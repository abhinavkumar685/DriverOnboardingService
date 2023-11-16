package com.intuit.craftdemo.entity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@Document(collection = "tokens")
public class AuthenticationToken {

    @Id
    private String id;

    @Field(name = "token")
    private String token;

    @Field(name = "createdDate")
    private Date createdDate;

    @Field(name = "userEmail")
    private String userEmail;
}
