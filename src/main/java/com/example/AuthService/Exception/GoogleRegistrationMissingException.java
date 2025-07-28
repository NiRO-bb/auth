package com.example.AuthService.Exception;

/**
 * Custom exception for cases when authenticated user hasn't google registration.
 */
public class GoogleRegistrationMissingException extends RuntimeException {

    public GoogleRegistrationMissingException(String message) {
        super(message);
    }

}
