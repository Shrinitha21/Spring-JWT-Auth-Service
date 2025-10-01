package com.Auth.AuthnenticationUsingJWT.jwt_demo.ExceptionHandling;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
