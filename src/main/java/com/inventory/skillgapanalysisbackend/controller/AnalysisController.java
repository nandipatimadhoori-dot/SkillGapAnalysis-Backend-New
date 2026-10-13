
package com.inventory.skillgapanalysisbackend.controller;

import com.inventory.skillgapanalysisbackend.entity.StudentProfile;
import com.inventory.skillgapanalysisbackend.repository.StudentProfileRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

 private final StudentProfileRepository profiles;

 public AnalysisController(StudentProfileRepository profiles) {
  this.profiles = profiles;
 }

 private Set<String> parse(String skills) {
  Set<String> result = new LinkedHashSet<>();

  if (skills == null || skills.isBlank()) {
   return result;
  }

  for (String skill : skills.toLowerCase().split("[,;\\n]")) {
   if (!skill.trim().isEmpty()) {
    result.add(skill.trim());
   }
  }

  return result;
 }

 @GetMapping("/{email}")
 public ResponseEntity<?> analyze(@PathVariable String email) {

  Optional<StudentProfile> optionalProfile =
          profiles.findByEmail(email.trim().toLowerCase());

  if (optionalProfile.isEmpty()) {
   return ResponseEntity.status(404).body(
           Map.of("error", "Complete your profile first")
   );
  }

  StudentProfile profile = optionalProfile.get();

  Set<String> currentSkills = parse(profile.getSkills());

  String interest = profile.getCareerInterest() == null
          ? "software developer"
          : profile.getCareerInterest().toLowerCase();

  Map<String, List<String>> roles = new LinkedHashMap<>();

  roles.put("Frontend Developer",
          List.of("html", "css", "javascript", "react", "git"));

  roles.put("Java Backend Developer",
          List.of("java", "spring boot", "sql", "rest api", "git"));

  roles.put("Data Analyst",
          List.of("sql", "excel", "python", "statistics", "power bi"));

  roles.put("AI/ML Engineer",
          List.of("python", "machine learning", "statistics",
                  "pandas", "scikit-learn"));

  List<Map<String, Object>> results = new ArrayList<>();

  for (Map.Entry<String, List<String>> entry : roles.entrySet()) {

   String jobTitle = entry.getKey();

   Set<String> requiredSkills =
           new LinkedHashSet<>(entry.getValue());

   Set<String> missingSkills =
           new LinkedHashSet<>(requiredSkills);

   missingSkills.removeAll(currentSkills);

   Set<String> matchedSkills =
           intersection(requiredSkills, currentSkills);

   int score = (int) Math.round(
           100.0 * matchedSkills.size() / requiredSkills.size()
   );

   boolean matchesInterest =
           (interest.contains("java")
                   && jobTitle.contains("Java"))
                   || (interest.contains("data")
                   && jobTitle.contains("Data"))
                   || ((interest.contains("ai")
                   || interest.contains("machine learning"))
                   && jobTitle.contains("AI/ML"))
                   || (interest.contains("front")
                   && jobTitle.contains("Frontend"));

   if (matchesInterest) {
    score = Math.min(100, score + 10);
   }

   Map<String, Object> recommendation = new LinkedHashMap<>();

   recommendation.put("jobTitle", jobTitle);
   recommendation.put("suitabilityScore", score);
   recommendation.put("requiredSkills", requiredSkills);
   recommendation.put("matchedSkills", matchedSkills);
   recommendation.put("missingSkills", missingSkills);
   recommendation.put("suggestion",
           "Learn the missing skills and build a small portfolio project.");

   results.add(recommendation);
  }

  results.sort((first, second) ->
          Integer.compare(
                  (Integer) second.get("suitabilityScore"),
                  (Integer) first.get("suitabilityScore")
          )
  );

  Map<String, Object> response = new LinkedHashMap<>();
  response.put("email", profile.getEmail());
  response.put("currentSkills", currentSkills);
  response.put("recommendations", results);
  response.put("method",
          "Rule-based skill overlap score; not a trained ML prediction");

  return ResponseEntity.ok(response);
 }

 private Set<String> intersection(
         Set<String> required,
         Set<String> current) {

  Set<String> matched = new LinkedHashSet<>(required);
  matched.retainAll(current);
  return matched;
 }
}
