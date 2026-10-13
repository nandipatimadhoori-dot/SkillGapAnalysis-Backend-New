
package com.inventory.skillgapanalysisbackend.service;

import com.inventory.skillgapanalysisbackend.entity.AppUser;
import com.inventory.skillgapanalysisbackend.repository.AppUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final SecretKey secretKey;
    private final long expirationMs;

    public AuthService(
            AppUserRepository users,
            PasswordEncoder encoder,
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {

        this.users = users;
        this.encoder = encoder;
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public Map<String, Object> login(String email, String password) {
        AppUser user = users.findByEmail(
                        email.trim().toLowerCase())
                .filter(u -> encoder.matches(
                        password, u.getPasswordHash()))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"));

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        String token = Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("token", token);
        response.put("tokenType", "Bearer");
        response.put("message", "Login successful");

        return response;
    }
}
