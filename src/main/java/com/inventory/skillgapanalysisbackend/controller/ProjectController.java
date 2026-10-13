
package com.inventory.skillgapanalysisbackend.controller;

import com.inventory.skillgapanalysisbackend.entity.Project;
import com.inventory.skillgapanalysisbackend.entity.StudentProfile;
import com.inventory.skillgapanalysisbackend.repository.ProjectRepository;
import com.inventory.skillgapanalysisbackend.repository.StudentProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projects;
    private final StudentProfileRepository profiles;

    public ProjectController(
            ProjectRepository projects,
            StudentProfileRepository profiles) {
        this.projects = projects;
        this.profiles = profiles;
    }

    // Get all available projects
    @GetMapping
    public List<Project> getAllProjects() {
        return projects.findAll();
    }

    // Get one project by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        return projects.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Recommend projects based on student skills
    @GetMapping("/recommend/{email}")
    public ResponseEntity<?> recommend(@PathVariable String email) {

        Optional<StudentProfile> optionalProfile =
                profiles.findByEmail(email.trim().toLowerCase());

        if (optionalProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        StudentProfile profile = optionalProfile.get();

        Set<String> studentSkills = Arrays.stream(
                        Optional.ofNullable(profile.getSkills())
                                .orElse("")
                                .split("[,;\\n]"))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(skill -> !skill.isBlank())
                .collect(Collectors.toSet());

        List<Map<String, Object>> recommendations =
                projects.findAll().stream()
                        .map(project -> {

                            Set<String> requiredSkills =
                                    Arrays.stream(
                                                    Optional.ofNullable(
                                                                    project.getRequiredSkills())
                                                            .orElse("")
                                                            .split("[,;\\n]"))
                                            .map(String::trim)
                                            .map(String::toLowerCase)
                                            .filter(skill -> !skill.isBlank())
                                            .collect(Collectors.toSet());

                            Set<String> missing = new TreeSet<>(requiredSkills);
                            missing.removeAll(studentSkills);

                            Set<String> matched = new TreeSet<>(requiredSkills);
                            matched.retainAll(studentSkills);

                            int percentage = requiredSkills.isEmpty()
                                    ? 0
                                    : matched.size() * 100 / requiredSkills.size();

                            Map<String, Object> result = new LinkedHashMap<>();
                            result.put("id", project.getId());
                            result.put("title", project.getTitle());
                            result.put("description", project.getDescription());
                            result.put("difficulty", project.getDifficulty());
                            result.put("projectType", project.getProjectType());
                            result.put("matchedSkills", matched);
                            result.put("missingSkills", missing);
                            result.put("matchPercentage", percentage);

                            return result;
                        })
                        .sorted((a, b) -> Integer.compare(
                                (Integer) b.get("matchPercentage"),
                                (Integer) a.get("matchPercentage")))
                        .toList();

        return ResponseEntity.ok(recommendations);
    }
}
