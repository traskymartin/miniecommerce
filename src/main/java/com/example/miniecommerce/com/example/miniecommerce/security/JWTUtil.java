package com.example.miniecommerce.com.example.miniecommerce.security;

import com.example.miniecommerce.com.example.miniecommerce.entity.Role;
import com.example.miniecommerce.com.example.miniecommerce.entity.User;
import com.example.miniecommerce.com.example.miniecommerce.repos.UserRepo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    // Secure secret key (Use environment variables or configuration instead of hardcoded value)
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(
            "YourSuperSecureSecretKeyMustBeAtLeast64BytesLongAndRandom".getBytes(StandardCharsets.UTF_8)
    );

    // Expiration time
    private static final long JWT_EXPIRATION_MS = 86400000; // 24 hours

    private final UserRepo userRepo;

    public JWTUtil(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Generate Token
    public String generateToken(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> roles = user.getRoles();
        String rolesString = roles.stream().map(Role::getName).collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesString)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(secretKey, SignatureAlgorithm.HS256) // Specify algorithm explicitly
                .compact();
    }

    // Extract UserName
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extract Roles
    public Set<String> extractRoles(String token) {
        String rolesString = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", String.class);

        return rolesString == null || rolesString.isEmpty() ? Collections.emptySet() :
                new HashSet<>(Arrays.asList(rolesString.split(",")));
    }

    // Token Validation
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

