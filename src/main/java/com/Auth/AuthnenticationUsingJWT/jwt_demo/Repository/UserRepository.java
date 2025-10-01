package com.Auth.AuthnenticationUsingJWT.jwt_demo.Repository;



import com.Auth.AuthnenticationUsingJWT.jwt_demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

