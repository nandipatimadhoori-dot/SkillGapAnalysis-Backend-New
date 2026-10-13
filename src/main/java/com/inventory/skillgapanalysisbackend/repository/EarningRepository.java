package com.inventory.skillgapanalysisbackend.repository;
import com.inventory.skillgapanalysisbackend.entity.Earning; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface EarningRepository extends JpaRepository<Earning,Long> { List<Earning> findByStudentUserId(Long userId); }
