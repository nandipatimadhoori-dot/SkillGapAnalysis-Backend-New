package com.inventory.skillgapanalysisbackend.controller;
import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestController public class HomeController { @GetMapping("/") public Map<String,String> home(){return Map.of("project","AI Skill Gap Analysis","status","Backend is running");} @GetMapping("/api/health") public Map<String,String> health(){return Map.of("status","UP");} }
