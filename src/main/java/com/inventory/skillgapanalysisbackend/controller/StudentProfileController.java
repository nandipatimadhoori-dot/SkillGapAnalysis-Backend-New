
package com.inventory.skillgapanalysisbackend.controller;

import com.inventory.skillgapanalysisbackend.entity.StudentProfile;
import com.inventory.skillgapanalysisbackend.repository.StudentProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class StudentProfileController {

    private final StudentProfileRepository profiles;

    public StudentProfileController(StudentProfileRepository profiles) {
        this.profiles = profiles;
    }

    @PostMapping
    public ResponseEntity<?> saveProfile(
            @RequestBody StudentProfile request) {

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email is required"));
        }

        String email = request.getEmail().trim().toLowerCase();

        StudentProfile profile = profiles.findByEmail(email)
                .orElseGet(StudentProfile::new);

        profile.setEmail(email);
        profile.setEducation(request.getEducation());
        profile.setDegree(request.getDegree());
        profile.setCareerInterest(request.getCareerInterest());
        profile.setSkills(request.getSkills());
        profile.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(profiles.save(profile));
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        return profiles.findByEmail(email.trim().toLowerCase())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
