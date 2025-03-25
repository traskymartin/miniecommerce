package com.example.miniecommerce.com.example.miniecommerce.repos;

import com.example.miniecommerce.com.example.miniecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
