package com.example.taskapi.service;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.Task.Priority;
import com.example.taskapi.model.Task.Status;
import com.example.taskapi.model.Category;
import com.example.taskapi.repository.TaskRepository;
import com.example.taskapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }
    
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    
    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }
    
    public List<Task> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority);
    }
    
    public List<Task> getTasksByCategory(Long categoryId) {
        return taskRepository.findByCategoryId(categoryId);
    }
    
    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDateTime.now());
    }
    
    public Task createTask(Task task) {
        // Validate category if provided
        if (task.getCategory() != null && task.getCategory().getId() != null) {
            Category category = categoryRepository.findById(task.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + task.getCategory().getId()));
            task.setCategory(category);
        }
        
        return taskRepository.save(task);
    }
    
    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        
        // Only update non-null fields
        if (taskDetails.getTitle() != null && !taskDetails.getTitle().trim().isEmpty()) {
            task.setTitle(taskDetails.getTitle());
        }
        if (taskDetails.getDescription() != null) {
            task.setDescription(taskDetails.getDescription());
        }
        if (taskDetails.getPriority() != null) {
            task.setPriority(taskDetails.getPriority());
        }
        if (taskDetails.getStatus() != null) {
            task.setStatus(taskDetails.getStatus());
        }
        if (taskDetails.getDueDate() != null) {
            task.setDueDate(taskDetails.getDueDate());
        }
        
        // Update category if provided
        if (taskDetails.getCategory() != null && taskDetails.getCategory().getId() != null) {
            Category category = categoryRepository.findById(taskDetails.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + taskDetails.getCategory().getId()));
            task.setCategory(category);
        } else if (taskDetails.getCategory() != null && taskDetails.getCategory().getId() == null) {
            // Explicitly set to null if category object provided but id is null
            task.setCategory(null);
        }
        
        return taskRepository.save(task);
    }
    
    public Task updateTaskStatus(Long id, Status status) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        
        task.setStatus(status);
        return taskRepository.save(task);
    }
    
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
    
    public long getTaskCountByStatus(Status status) {
        return taskRepository.countByStatus(status);
    }
}
