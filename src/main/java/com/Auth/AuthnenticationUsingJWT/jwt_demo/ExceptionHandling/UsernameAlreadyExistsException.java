package com.Auth.AuthnenticationUsingJWT.jwt_demo.ExceptionHandling;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

}
