package com.inventory.skillgapanalysisbackend.entity;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="project_tasks")
public class ProjectTask {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String title; @Column(length=4000) private String description; private String requiredSkills; private Double rewardAmount=0.0; private String status="OPEN"; private Long companyUserId; private LocalDateTime createdAt=LocalDateTime.now();
 public Long getId(){return id;} public String getTitle(){return title;} public void setTitle(String v){title=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;} public String getRequiredSkills(){return requiredSkills;} public void setRequiredSkills(String v){requiredSkills=v;} public Double getRewardAmount(){return rewardAmount;} public void setRewardAmount(Double v){rewardAmount=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public Long getCompanyUserId(){return companyUserId;} public void setCompanyUserId(Long v){companyUserId=v;} public LocalDateTime getCreatedAt(){return createdAt;}
}
