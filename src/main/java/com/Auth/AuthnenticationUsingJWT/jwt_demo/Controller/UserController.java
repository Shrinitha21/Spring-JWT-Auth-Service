package com.Auth.AuthnenticationUsingJWT.jwt_demo.Controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/hello")
    public Map<String, String> hello(Principal principal) {
        return Map.of("message", "Hello!",
                "user", principal == null ? "anonymous" : principal.getName()
        );
    }
}

