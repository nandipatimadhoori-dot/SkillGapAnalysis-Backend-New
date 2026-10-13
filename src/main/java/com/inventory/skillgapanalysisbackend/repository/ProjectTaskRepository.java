package com.inventory.skillgapanalysisbackend.repository;
import com.inventory.skillgapanalysisbackend.entity.ProjectTask; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface ProjectTaskRepository extends JpaRepository<ProjectTask,Long> { List<ProjectTask> findByStatus(String status); }
