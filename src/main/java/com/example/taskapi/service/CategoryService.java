package com.example.taskapi.service;

import com.example.taskapi.model.Category;
import com.example.taskapi.repository.CategoryRepository;
import com.example.taskapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }
    
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }
        return categoryRepository.save(category);
    }
    
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        
        // Only update non-null, non-empty fields
        if (categoryDetails.getName() != null && !categoryDetails.getName().trim().isEmpty()) {
            // Check if new name conflicts with existing category
            if (!category.getName().equals(categoryDetails.getName()) && 
                categoryRepository.existsByName(categoryDetails.getName())) {
                throw new IllegalArgumentException("Category with name '" + categoryDetails.getName() + "' already exists");
            }
            category.setName(categoryDetails.getName());
        }
        if (categoryDetails.getDescription() != null) {
            category.setDescription(categoryDetails.getDescription());
        }
        
        return categoryRepository.save(category);
    }
    
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        
        // Check if category has associated tasks
        long taskCount = taskRepository.findByCategoryId(id).size();
        if (taskCount > 0) {
            throw new IllegalArgumentException("Cannot delete category with id: " + id + 
                ". It has " + taskCount + " associated task(s). Remove or reassign tasks first.");
        }
        
        categoryRepository.deleteById(id);
    }
}
