package com.intuit.craftdemo.repository;

import com.intuit.craftdemo.entity.DriverDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverDocumentRepository extends MongoRepository<DriverDocument, String> {
    DriverDocument findByDriverEmail(String email);
}
