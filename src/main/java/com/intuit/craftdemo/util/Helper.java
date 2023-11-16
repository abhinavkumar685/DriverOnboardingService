package com.intuit.craftdemo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.UUID;

public class Helper {
    private static final Logger log = LoggerFactory.getLogger(Helper.class);
    
    public static boolean isNotNull(Object obj){
        return obj != null;
    }
    public static String generateUserToken() {
        return UUID.randomUUID().toString();
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isEmpty(Collection coll) {
        return coll == null || coll.isEmpty();
    }

    public static String getEncryptedPassword(String rawPassword) {
        String encryptedPassword = rawPassword;
        try {
            encryptedPassword = hashPassword(rawPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("Password hashing failed with error: {}", e.getMessage());
        }
        return encryptedPassword;
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter
                .printHexBinary(digest).toUpperCase();
    }
    
}
