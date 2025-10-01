package com.Auth.AuthnenticationUsingJWT.jwt_demo.Controller;



import com.Auth.AuthnenticationUsingJWT.jwt_demo.DTOs.AuthRequest;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.DTOs.AuthResponse;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.ExceptionHandling.InvalidCredentialsException;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.ExceptionHandling.UsernameAlreadyExistsException;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Model.User;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Service.UserService;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Security.JwtUtil;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    // create default admin
    @PostConstruct
    public void init() {
        userService.findOptionalByUsername("admin").orElseGet(() ->
                userService.register("admin", "admin123", Set.of("ROLE_ADMIN")));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody AuthRequest req) {
        logger.info("Signup attempt for username: {}", req.getUsername());

        if (userService.findOptionalByUsername(req.getUsername()).isPresent()) {
            logger.warn("Signup failed: username already exists - {}", req.getUsername());
            throw new UsernameAlreadyExistsException("Username already exists: " + req.getUsername());
        }

        userService.register(req.getUsername(), req.getPassword(), Set.of("ROLE_USER"));
        logger.info("User registered successfully: {}", req.getUsername());
        return ResponseEntity.ok("User registered successfully!");
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        logger.info("Login attempt for username: {}", req.getUsername());

        var uOpt = userService.findOptionalByUsername(req.getUsername());

        if (uOpt.isEmpty() || !passwordEncoder.matches(req.getPassword(), uOpt.get().getPassword())) {
            logger.warn("Login failed for username: {}", req.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }

        User u = uOpt.get();
        String token = jwtUtil.generateToken(u.getUsername(), u.getRoles());
        logger.info("JWT issued for username: {}", req.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}

