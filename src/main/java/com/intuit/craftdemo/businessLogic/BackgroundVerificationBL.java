package com.intuit.craftdemo.businessLogic;

import com.intuit.craftdemo.entity.Driver;
import com.intuit.craftdemo.exception.DriverOnbaordingException;
import com.intuit.craftdemo.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class BackgroundVerificationBL {
    private final Logger log = LoggerFactory.getLogger(BackgroundVerificationBL.class);
    public void verifyBackground(Driver driver) {
        // Simulate a background verification process
        if (!simulateBackgroundCheck(driver)) {
            log.error("Background verification failed for Driver {}", driver.getEmail());
            throw new DriverOnbaordingException(String.format(ResponseMessage.getDRIVER_BACKGROUND_VERIFICATION_FAILED(), 
                    driver.getEmail()), ResponseMessage.ErrorCodes.DRIVER_BACKGROUND_VERIFICATION_FAILED.toString());
        }
   }

    private boolean simulateBackgroundCheck(Driver driver) {
        /*
         * Simulate Driver Background Check logic here
         * This can be decoupled from the service and can use pub-sub to notify other service
         * Just returning true for simplicity
         */
        log.info("Background Verification successful for Driver {}", driver);
        return true;
    }
}
