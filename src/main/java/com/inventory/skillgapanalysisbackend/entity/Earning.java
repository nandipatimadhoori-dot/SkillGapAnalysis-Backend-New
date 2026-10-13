package com.inventory.skillgapanalysisbackend.entity;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="earnings")
public class Earning {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private Long studentUserId; private Long taskId; private Double amount; private String status="PENDING"; private LocalDateTime createdAt=LocalDateTime.now();
 public Long getId(){return id;} public Long getStudentUserId(){return studentUserId;} public void setStudentUserId(Long v){studentUserId=v;} public Long getTaskId(){return taskId;} public void setTaskId(Long v){taskId=v;} public Double getAmount(){return amount;} public void setAmount(Double v){amount=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public LocalDateTime getCreatedAt(){return createdAt;}
}
