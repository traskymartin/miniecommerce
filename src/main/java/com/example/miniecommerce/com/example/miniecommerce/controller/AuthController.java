package com.example.miniecommerce.com.example.miniecommerce.controller;

import com.example.miniecommerce.com.example.miniecommerce.dto.RegisterRequest;
import com.example.miniecommerce.com.example.miniecommerce.entity.Role;
import com.example.miniecommerce.com.example.miniecommerce.entity.User;
import com.example.miniecommerce.com.example.miniecommerce.repos.RoleRepo;
import com.example.miniecommerce.com.example.miniecommerce.repos.UserRepo;
import com.example.miniecommerce.com.example.miniecommerce.security.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Register User
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        // Check if userName Already exit in DB
        if (userRepo.findByUsername(registerRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already in use", HttpStatus.BAD_REQUEST);
        }

        User user = new User();

        user.setUsername(registerRequest.getUsername());
        String encoderPass = passwordEncoder.encode(registerRequest.getPassword());
        System.out.println(encoderPass);
        user.setPassword(encoderPass);

        // Conver the role name to role Entity and assign to user

        Set<Role> roles = new HashSet<>();

        for (String Nrole : registerRequest.getRoles()) {
            Role role = roleRepo.findByName(Nrole).orElseThrow(() -> new RuntimeException("Role not found " + Nrole));

            roles.add(role);
        }

        user.setRoles(roles);
        userRepo.save(user);

        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    // Login APi

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()));
        }catch (Exception e) {
            System.out.println("Exception : " + e);
        }

        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(token);

    }

}
