package com.Auth.AuthnenticationUsingJWT.jwt_demo.Controller;



import com.Auth.AuthnenticationUsingJWT.jwt_demo.DTOs.AuthRequest;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.DTOs.AuthResponse;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Model.User;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Security.JwtUtil;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    void testSignup_Success() {
        AuthRequest req = new AuthRequest();
        req.setUsername("shrini");
        req.setPassword("pass");

        when(userService.findByUsername("shrini")).thenReturn(Optional.empty());
        when(userService.register(anyString(), anyString(), anySet())).thenReturn(
                User.builder().username("shrini").roles(Set.of("ROLE_USER")).build()
        );

        ResponseEntity<String> response = authController.signup(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("registered");
        verify(userService, times(1)).register(eq("shrini"), eq("pass"), eq(Set.of("ROLE_USER")));
    }

    @Test
    void testSignup_UsernameExists() {
        AuthRequest req = new AuthRequest();
        req.setUsername("shrini");
        req.setPassword("pass");

        when(userService.findByUsername("shrini")).thenReturn(Optional.of(new User()));

        ResponseEntity<String> response = authController.signup(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Username exists");
        verify(userService, never()).register(anyString(), anyString(), anySet());
    }

    @Test
    void testLogin_Success() {
        AuthRequest req = new AuthRequest();
        req.setUsername("shrini");
        req.setPassword("pass");

        User mockUser = User.builder().username("shrini").password("encodedPass").roles(Set.of("ROLE_USER")).build();

        when(userService.findByUsername("shrini")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("pass", "encodedPass")).thenReturn(true);
        when(jwtUtil.generateToken("shrini", mockUser.getRoles())).thenReturn("jwtToken");

        ResponseEntity<AuthResponse> response = authController.login(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getToken()).isEqualTo("jwtToken");
    }

    @Test
    void testLogin_Failure_InvalidUser() {
        AuthRequest req = new AuthRequest();
        req.setUsername("unknown");
        req.setPassword("pass");

        when(userService.findByUsername("unknown")).thenReturn(Optional.empty());

        ResponseEntity<AuthResponse> response = authController.login(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    void testLogin_Failure_WrongPassword() {
        AuthRequest req = new AuthRequest();
        req.setUsername("shrini");
        req.setPassword("wrongPass");

        User mockUser = User.builder().username("shrini").password("encodedPass").roles(Set.of("ROLE_USER")).build();

        when(userService.findByUsername("shrini")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        ResponseEntity<AuthResponse> response = authController.login(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }
}
