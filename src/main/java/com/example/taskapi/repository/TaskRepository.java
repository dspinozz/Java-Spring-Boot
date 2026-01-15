package com.example.taskapi.repository;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.Task.Priority;
import com.example.taskapi.model.Task.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByStatus(Status status);
    
    List<Task> findByPriority(Priority priority);
    
    List<Task> findByCategoryId(Long categoryId);
    
    List<Task> findByStatusAndPriority(Status status, Priority priority);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.status != 'COMPLETED'")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId")
    List<Task> findByCategory(@Param("categoryId") Long categoryId);
    
    long countByStatus(Status status);
}
