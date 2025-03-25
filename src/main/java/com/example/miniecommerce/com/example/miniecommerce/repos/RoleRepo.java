package com.example.miniecommerce.com.example.miniecommerce.repos;


import com.example.miniecommerce.com.example.miniecommerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleName);
}