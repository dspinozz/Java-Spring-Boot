package com.example.taskapi.controller;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.Task.Priority;
import com.example.taskapi.model.Task.Status;
import com.example.taskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    private final TaskService taskService;
    
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }
    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable Priority priority) {
        return ResponseEntity.ok(taskService.getTasksByPriority(priority));
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Task>> getTasksByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(taskService.getTasksByCategory(categoryId));
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }
    
    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task) {
        try {
            Task created = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, 
                                           @Valid @RequestBody Task task) {
        try {
            Task updated = taskService.updateTask(id, task);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long id, 
                                                 @RequestBody Status status) {
        if (status == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Status is required");
            return ResponseEntity.badRequest().body(error);
        }
        try {
            Task updated = taskService.updateTaskStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<TaskStats> getTaskStats() {
        TaskStats stats = new TaskStats(
            taskService.getTaskCountByStatus(Status.PENDING),
            taskService.getTaskCountByStatus(Status.IN_PROGRESS),
            taskService.getTaskCountByStatus(Status.COMPLETED),
            taskService.getTaskCountByStatus(Status.CANCELLED)
        );
        return ResponseEntity.ok(stats);
    }
    
    // Inner class for stats response
    public static class TaskStats {
        private final long pending;
        private final long inProgress;
        private final long completed;
        private final long cancelled;
        
        public TaskStats(long pending, long inProgress, long completed, long cancelled) {
            this.pending = pending;
            this.inProgress = inProgress;
            this.completed = completed;
            this.cancelled = cancelled;
        }
        
        public long getPending() { return pending; }
        public long getInProgress() { return inProgress; }
        public long getCompleted() { return completed; }
        public long getCancelled() { return cancelled; }
    }
}
