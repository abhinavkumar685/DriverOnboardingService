package com.intuit.craftdemo.service;


import com.intuit.craftdemo.businessLogic.DocumentVerificationBL;
import com.intuit.craftdemo.businessLogic.DriverDeviceBL;
import com.intuit.craftdemo.dto.RequestParams;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.exception.DriverOnbaordingException;
import com.intuit.craftdemo.util.ResponseMessage;
import com.intuit.craftdemo.util.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class OnboardingService {
    @Autowired
    private DocumentVerificationBL documentVerificationBL;
    @Autowired
    private DriverService driverService;
    @Autowired
    private DriverDeviceBL driverDeviceBL;
    
    private final Logger log = LoggerFactory.getLogger(OnboardingService.class);

    public void onboardDriver(Driver driver, Map<RequestParams, MultipartFile> filesMap) {
        // These steps can be decoupled using kafka events
        uploadDocuments(driver, filesMap);
        verifyDocuments(driver);
        shipDevice(driver);
        completeOnboarding(driver);
    }

    private void completeOnboarding(Driver driver) {
        // Implement any other Business Logic to finally Onboard the driver
        driverService.updateDriverStatus(driver, UserStatus.ONBOARDING_COMPLETED);
        log.info("Onboarding successful for driver: {}", driver.getEmail());
    }

    private void shipDevice(Driver driver) {
        driverDeviceBL.shipTrackingDevice(driver);
        driverService.updateDriverStatus(driver, UserStatus.DEVICE_SHIPPED);
        log.info("Device successfully shipped for driver: {}", driver.getEmail());
    }

    private void verifyDocuments(Driver driver) {
        documentVerificationBL.verifyDocument(driver);
        driverService.updateDriverStatus(driver, UserStatus.DOCUMENT_VERIFIED);
        log.info("Document verification successful for driver: {}", driver.getEmail());
    }

    private void uploadDocuments(Driver driver, Map<RequestParams, MultipartFile> filesMap) {
        try {
            documentVerificationBL.uploadDocument(driver, filesMap);
        } catch (Exception e) {
            throw new DriverOnbaordingException(String.format(ResponseMessage.getDRIVER_DOCUMENT_SUBMISSION_FAILED(), driver.getEmail()),
                    ResponseMessage.ErrorCodes.DOCUMENT_SUBMISSION_FAILED.toString());
        }
        driverService.updateDriverStatus(driver, UserStatus.DOCUMENT_SUBMITTED);
        log.info("Document uploaded successful for driver: {}", driver.getEmail());
    }
}
