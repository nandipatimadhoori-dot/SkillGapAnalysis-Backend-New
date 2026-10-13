
// File: ProfileController.java

package com.inventory.skillgapanalysisbackend.controller;

import com.inventory.skillgapanalysisbackend.dto.ProfileRequest;
import com.inventory.skillgapanalysisbackend.entity.StudentProfile;
import com.inventory.skillgapanalysisbackend.repository.StudentProfileRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

 private final StudentProfileRepository profiles;

 public ProfileController(StudentProfileRepository profiles) {
  this.profiles = profiles;
 }

 // Get a student's profile
 @GetMapping("/{email}")
 public ResponseEntity<?> get(@PathVariable String email) {

  if (email == null || email.isBlank()) {
   return ResponseEntity.badRequest().body(
           Map.of("error", "Email is required")
   );
  }

  String normalizedEmail = email.trim().toLowerCase();

  StudentProfile profile = profiles
          .findByEmail(normalizedEmail)
          .orElseGet(() -> {
           StudentProfile emptyProfile = new StudentProfile();
           emptyProfile.setEmail(normalizedEmail);
           emptyProfile.setEducation("");
           emptyProfile.setCareerInterest("");
           emptyProfile.setSkills("");
           return emptyProfile;
          });

  return ResponseEntity.ok(profile);
 }

 // Create or update a student's profile
 @PutMapping("/{email}")
 public ResponseEntity<?> save(
         @PathVariable String email,
         @RequestBody ProfileRequest request) {

  if (email == null || email.isBlank()) {
   return ResponseEntity.badRequest().body(
           Map.of("error", "Email is required")
   );
  }

  if (request == null) {
   return ResponseEntity.badRequest().body(
           Map.of("error", "Profile details are required")
   );
  }

  String normalizedEmail = email.trim().toLowerCase();

  StudentProfile profile = profiles
          .findByEmail(normalizedEmail)
          .orElseGet(StudentProfile::new);

  profile.setEmail(normalizedEmail);
  profile.setEducation(request.getEducation());
  profile.setCareerInterest(request.getCareerInterest());
  profile.setSkills(request.getSkills());
  profile.setUpdatedAt(LocalDateTime.now());

  StudentProfile savedProfile = profiles.save(profile);

  return ResponseEntity.ok(savedProfile);
 }
}
