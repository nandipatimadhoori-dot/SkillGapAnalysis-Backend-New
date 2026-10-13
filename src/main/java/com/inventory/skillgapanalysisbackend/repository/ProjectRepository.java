package com.inventory.skillgapanalysisbackend.repository;

import com.inventory.skillgapanalysisbackend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}