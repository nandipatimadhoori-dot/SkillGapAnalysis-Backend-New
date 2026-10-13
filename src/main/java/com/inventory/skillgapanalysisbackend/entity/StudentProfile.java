
package com.inventory.skillgapanalysisbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(nullable = false, unique = true)
 private String email;

 private String education;
 private String degree;
 private String careerInterest;

 @Column(length = 2000)
 private String skills;

 private LocalDateTime updatedAt = LocalDateTime.now();

 public Long getId() { return id; }
 public String getEmail() { return email; }
 public void setEmail(String email) { this.email = email; }

 public String getEducation() { return education; }
 public void setEducation(String education) { this.education = education; }

 public String getDegree() { return degree; }
 public void setDegree(String degree) { this.degree = degree; }

 public String getCareerInterest() { return careerInterest; }
 public void setCareerInterest(String careerInterest) {
  this.careerInterest = careerInterest;
 }

 public String getSkills() { return skills; }
 public void setSkills(String skills) { this.skills = skills; }

 public LocalDateTime getUpdatedAt() { return updatedAt; }
 public void setUpdatedAt(LocalDateTime updatedAt) {
  this.updatedAt = updatedAt;
 }
}
