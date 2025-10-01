package com.Auth.AuthnenticationUsingJWT.jwt_demo.DTOs;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
}

