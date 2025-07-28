package com.example.AuthService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * Handles some exceptions from controller classes.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Catches GoogleRegistrationMissingException and handles them.
     *
     * @param exception catched exception
     * @return ResponseEntity with error message and http status NOT_FOUND
     */
    @ExceptionHandler(GoogleRegistrationMissingException.class)
    public ResponseEntity<?> handleGoogleRegistrationMissingException(GoogleRegistrationMissingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Catches BadCredentialsExceptions and handles them.
     *
     * @param exception catched exception
     * @return ResponseEntity with error message and http status NOT_FOUND
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Catches custom DataNotFoundExceptions and handles them.
     *
     * @param exception catched exception
     * @return ResponseEntity with error message and http status NOT_FOUND
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> handleDataNotFoundException(DataNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Catches AccessDeniedExceptions and handles them.
     *
     * @param exception catched exception
     * @return ResponseEntity with error message and http status FORBIDDEN
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Catches RuntimeExceptions and handles them.
     *
     * @param exception catched exception
     * @return ResponseEntity with error message and http status INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
