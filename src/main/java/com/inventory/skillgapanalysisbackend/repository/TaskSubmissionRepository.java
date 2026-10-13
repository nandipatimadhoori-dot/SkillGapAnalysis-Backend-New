package com.inventory.skillgapanalysisbackend.repository;
import com.inventory.skillgapanalysisbackend.entity.TaskSubmission; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface TaskSubmissionRepository extends JpaRepository<TaskSubmission,Long> { List<TaskSubmission> findByStudentUserId(Long userId); }
