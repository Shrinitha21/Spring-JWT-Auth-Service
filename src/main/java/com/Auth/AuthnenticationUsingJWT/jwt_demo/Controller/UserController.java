package com.Auth.AuthnenticationUsingJWT.jwt_demo.Controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @GetMapping("/hello")
    public Map<String, String> hello(Principal principal) {
        logger.info("User Loggedin successfully: {}", principal.getName());
        return Map.of("message", "Hello!",
                "user", principal == null ? "anonymous" : principal.getName()
        );
    }
}

