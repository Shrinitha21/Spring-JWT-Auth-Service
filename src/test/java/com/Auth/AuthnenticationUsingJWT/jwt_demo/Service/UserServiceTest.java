package com.Auth.AuthnenticationUsingJWT.jwt_demo.Service;

import com.Auth.AuthnenticationUsingJWT.jwt_demo.Model.User;
import com.Auth.AuthnenticationUsingJWT.jwt_demo.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegister_Success() {
        String username = "shrini";
        String rawPassword = "pass";
        Set<String> roles = Set.of("ROLE_USER");

        User mockUser = User.builder()
                .username(username)
                .password("encodedPass")
                .roles(roles)
                .build();

        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User savedUser = userService.register(username, rawPassword, roles);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(username);
        assertThat(savedUser.getRoles()).containsExactlyInAnyOrderElementsOf(roles);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByUsername_UserExists() {
        User mockUser = User.builder().username("shrini").build();
        when(userRepository.findByUsername("shrini")).thenReturn(Optional.of(mockUser));

        Optional<User> found = userService.findByUsername("shrini");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("shrini");
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<User> found = userService.findByUsername("unknown");

        assertThat(found).isEmpty();
    }
}
