package com.example.taskapi.controller;

import com.example.taskapi.repository.CategoryRepository;
import com.example.taskapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {
    
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public HealthController(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("database", "connected");
        health.put("tasks_count", taskRepository.count());
        health.put("categories_count", categoryRepository.count());
        return ResponseEntity.ok(health);
    }
}
