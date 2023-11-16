package com.intuit.craftdemo.businessLogic;

import com.intuit.craftdemo.dto.RequestParams;
import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.entity.DriverDocument;
import com.intuit.craftdemo.repository.DriverDocumentRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class DocumentVerificationBL {
    private final Logger log = LoggerFactory.getLogger(DocumentVerificationBL.class);
    
    @Autowired
    private DriverDocumentRepository driverDocumentRepository;
    
    public void uploadDocument(Driver driver, Map<RequestParams, MultipartFile> filesMap) throws IOException {
        // Documents can be uploaded into S3 bucket and corresponding link can be saved in DO
        log.info("Uploading documents for Driver: {}", driver.getEmail());
        DriverDocument driverDocument = DriverDocument.builder()
                .driverEmail(driver.getEmail())
                .photo(new Binary(BsonBinarySubType.BINARY, filesMap.get(RequestParams.PHOTO).getBytes()))
                .aadharCard(new Binary(BsonBinarySubType.BINARY, filesMap.get(RequestParams.AADHAR_CARD).getBytes()))
                .panCard(new Binary(BsonBinarySubType.BINARY, filesMap.get(RequestParams.PAN_CARD).getBytes()))
                .driverLicense(new Binary(BsonBinarySubType.BINARY, filesMap.get(RequestParams.DRIVER_LICENSE).getBytes()))
                .build();
        driverDocumentRepository.save(driverDocument);
    }
    
    public void verifyDocument(Driver driver) {
        // Simulate the Document verification process
        // You can implement logic here to determine if document verification passes
    }
    
}
