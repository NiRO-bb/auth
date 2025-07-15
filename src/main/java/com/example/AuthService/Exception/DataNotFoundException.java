package com.example.AuthService.Exception;

/**
 * Custom exception for cases when could not find some entity in database.
 */
public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String message) {
        super(message);
    }

}
