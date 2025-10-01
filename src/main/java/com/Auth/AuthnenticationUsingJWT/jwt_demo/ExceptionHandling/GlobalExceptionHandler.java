package com.Auth.AuthnenticationUsingJWT.jwt_demo.ExceptionHandling;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1️⃣ Handle custom exceptions
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        logger.error("User not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 2️⃣ Handle validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed: {}", ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("errors", fieldErrors);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameExists(UsernameAlreadyExistsException ex) {
        logger.warn("Username already exists: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT); // 409
    }

    // 5️⃣ Handle InvalidCredentialsException
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        logger.warn("Invalid login attempt: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED); // 401
    }

    // 3️⃣ Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        logger.error("Unexpected error: ", ex);
        return buildErrorResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper method to create structured error response
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", message);
        error.put("status", status.value());
        return new ResponseEntity<>(error, status);
    }
}

