package com.Auth.AuthnenticationUsingJWT.jwt_demo.Controller;



import com.Auth.AuthnenticationUsingJWT.jwt_demo.DTOs.AuthRequest;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.DTOs.AuthResponse;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Model.User;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Service.UserService;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Security.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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

    // create default admin
    @PostConstruct
    public void init() {
        userService.findByUsername("admin").orElseGet(() ->
                userService.register("admin", "admin123", Set.of("ROLE_ADMIN")));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AuthRequest req) {
        if (userService.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username exists");
        }
        userService.register(req.getUsername(), req.getPassword(), Set.of("ROLE_USER"));
        return ResponseEntity.ok("registered");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        var uOpt = userService.findByUsername(req.getUsername());
        if (uOpt.isEmpty() || !passwordEncoder.matches(req.getPassword(), uOpt.get().getPassword())) {
            return ResponseEntity.status(401).build();
        }
        User u = uOpt.get();
        String token = jwtUtil.generateToken(u.getUsername(), u.getRoles());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

