package com.example.miniecommerce.com.example.miniecommerce.controller;

import com.example.miniecommerce.com.example.miniecommerce.entity.User;
import com.example.miniecommerce.com.example.miniecommerce.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${role.admin}")
    private String adminRole;

    @Value("${role,user}")
    private String userRole;

    //End Point of user Prodected Request


    @GetMapping("/product-data")
    public ResponseEntity<String> getProductData(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);

            try {
                if (jwtUtil.validateToken(jwt)) {
                    String userName = jwtUtil.extractUsername(jwt);
                    Set<String> roles = jwtUtil.extractRoles(jwt);
                    if (roles.contains(adminRole)) {
                        return ResponseEntity.ok("Welcome " + userName + " - Role " + roles + " - Data");
                    } else if (roles.contains(userRole)) {
                        return ResponseEntity.ok("Welcome -> " + userName + " < - >  Role " + roles + " - Data");
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
                    }
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
    }

}
