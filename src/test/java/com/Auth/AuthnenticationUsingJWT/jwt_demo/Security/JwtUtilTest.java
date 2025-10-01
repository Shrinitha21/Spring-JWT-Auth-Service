package com.Auth.AuthnenticationUsingJWT.jwt_demo.Security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "mySecretKeymySecretKeymySecretKeymySecretKey"; // must be long enough
    private final long expiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret, expiration);
    }

    @Test
    void testGenerateAndParseToken() {
        String token = jwtUtil.generateToken("shrini", Set.of("ROLE_USER"));

        assertThat(token).isNotNull();
        assertThat(jwtUtil.getUsername(token)).isEqualTo("shrini");

        assertThat(jwtUtil.parseToken(token).getBody().get("roles", Set.class)).contains("ROLE_USER");
    }
}
