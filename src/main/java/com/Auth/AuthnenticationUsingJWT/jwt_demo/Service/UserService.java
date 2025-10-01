package com.Auth.AuthnenticationUsingJWT.jwt_demo.Service;



import com.Auth.AuthnenticationUsingJWT.jwt_demo.ExceptionHandling.UserNotFoundException;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Model.User;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
//import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(UserService.class);


    @Transactional
    public User register(String username, String rawPassword, Set<String> roles) {

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(roles)
                .build();
        logger.info("Registering user: {}" + user);
        return userRepository.save(user);
    }

    public Optional<User> findOptionalByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        logger.debug("Fetching user with username: {}", username);

        return Optional.ofNullable(userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UserNotFoundException("User not found with username: " + username);
                }));
    }
}

