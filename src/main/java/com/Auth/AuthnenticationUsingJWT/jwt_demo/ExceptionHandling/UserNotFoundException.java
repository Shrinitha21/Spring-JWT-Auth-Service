package com.Auth.AuthnenticationUsingJWT.jwt_demo.ExceptionHandling;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message)
    {
        super(message);
    }
}
