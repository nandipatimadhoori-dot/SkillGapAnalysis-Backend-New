
package com.inventory.skillgapanalysisbackend.controller;

import com.inventory.skillgapanalysisbackend.dto.AuthRequest;
import com.inventory.skillgapanalysisbackend.dto.RegisterRequest;
import com.inventory.skillgapanalysisbackend.entity.AppUser;
import com.inventory.skillgapanalysisbackend.repository.AppUserRepository;
import com.inventory.skillgapanalysisbackend.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

 private final AppUserRepository users;
 private final PasswordEncoder encoder;
 private final AuthService authService;

 public AuthController(
         AppUserRepository users,
         PasswordEncoder encoder,
         AuthService authService) {

  this.users = users;
  this.encoder = encoder;
  this.authService = authService;
 }

 // Register a new user
 @PostMapping("/register")
 public ResponseEntity<?> register(
         @Valid @RequestBody RegisterRequest request) {

  try {
   // Validate required fields
   if (request.getFullName() == null
           || request.getFullName().isBlank()) {

    return ResponseEntity
            .badRequest()
            .body(Map.of(
                    "error",
                    "Full name is required"
            ));
   }

   if (request.getEmail() == null
           || request.getEmail().isBlank()) {

    return ResponseEntity
            .badRequest()
            .body(Map.of(
                    "error",
                    "Email is required"
            ));
   }

   if (request.getPassword() == null
           || request.getPassword().isBlank()) {

    return ResponseEntity
            .badRequest()
            .body(Map.of(
                    "error",
                    "Password is required"
            ));
   }

   String fullName = request.getFullName().trim();
   String email = request.getEmail()
           .trim()
           .toLowerCase();

   // Check duplicate email
   if (users.existsByEmail(email)) {
    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of(
                    "error",
                    "Email already registered"
            ));
   }

   // Create user
   AppUser user = new AppUser();

   user.setFullName(fullName);
   user.setEmail(email);
   user.setPasswordHash(
           encoder.encode(request.getPassword())
   );

   String role = "COMPANY".equalsIgnoreCase(
           request.getRole()
   ) ? "COMPANY" : "STUDENT";

   user.setRole(role);

   AppUser savedUser = users.save(user);

   return ResponseEntity
           .status(HttpStatus.CREATED)
           .body(Map.of(
                   "id", savedUser.getId(),
                   "fullName", savedUser.getFullName(),
                   "email", savedUser.getEmail(),
                   "role", savedUser.getRole(),
                   "message", "Registration successful"
           ));

  } catch (Exception e) {
   // Log the real error in the Spring Boot console
   System.err.println(
           "Registration failed: " + e.getMessage()
   );

   return ResponseEntity
           .status(HttpStatus.INTERNAL_SERVER_ERROR)
           .body(Map.of(
                   "error",
                   "Registration failed. Check the backend console."
           ));
  }
 }

 // Login an existing user
 @PostMapping("/login")
 public ResponseEntity<?> login(
         @Valid @RequestBody AuthRequest request) {

  try {
   return ResponseEntity.ok(
           authService.login(
                   request.getEmail(),
                   request.getPassword()
           )
   );

  } catch (IllegalArgumentException e) {
   return ResponseEntity
           .status(HttpStatus.UNAUTHORIZED)
           .body(Map.of(
                   "error",
                   "Invalid email or password"
           ));
  }
 }

 // Logout
 @PostMapping("/logout")
 public ResponseEntity<?> logout() {

  return ResponseEntity.ok(
          Map.of(
                  "message",
                  "Logged out; remove the token from the client"
          )
  );
 }
}
