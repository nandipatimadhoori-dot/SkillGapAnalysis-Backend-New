package com.inventory.skillgapanalysisbackend.entity;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="task_submissions")
public class TaskSubmission {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private Long taskId; private Long studentUserId; @Column(length=4000) private String submissionUrl; @Column(length=4000) private String notes; private String status="SUBMITTED"; private LocalDateTime submittedAt=LocalDateTime.now();
 public Long getId(){return id;} public Long getTaskId(){return taskId;} public void setTaskId(Long v){taskId=v;} public Long getStudentUserId(){return studentUserId;} public void setStudentUserId(Long v){studentUserId=v;} public String getSubmissionUrl(){return submissionUrl;} public void setSubmissionUrl(String v){submissionUrl=v;} public String getNotes(){return notes;} public void setNotes(String v){notes=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public LocalDateTime getSubmittedAt(){return submittedAt;}
}
