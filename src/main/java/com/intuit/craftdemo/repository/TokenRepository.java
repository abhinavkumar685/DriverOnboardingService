package com.intuit.craftdemo.repository;

import com.intuit.craftdemo.entity.AuthenticationToken;
import com.intuit.craftdemo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<AuthenticationToken, String> {
    AuthenticationToken findTokenByUserEmail(String userEmail);
    AuthenticationToken findTokenByToken(String token);
}
