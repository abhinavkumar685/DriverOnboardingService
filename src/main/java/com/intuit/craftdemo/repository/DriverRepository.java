package com.intuit.craftdemo.repository;

import com.intuit.craftdemo.entity.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {
    Driver findByEmail(String email);
    List<Driver> findAll();
}
