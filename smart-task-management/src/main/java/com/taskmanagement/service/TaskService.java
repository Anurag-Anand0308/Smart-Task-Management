package com.taskmanagement.service;

import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.dto.response.DashboardDTO;
import com.taskmanagement.dto.request.CreateTaskRequest;
import com.taskmanagement.dto.request.UpdateTaskRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for task operations
 */
public interface TaskService {
    
    /**
     * Create a new task
     */
    TaskDTO createTask(Long userId, CreateTaskRequest request);
    
    /**
     * Get task by id
     */
    TaskDTO getTaskById(Long taskId, Long userId);
    
    /**
     * Get all tasks for a user with pagination
     */
    Page<TaskDTO> getAllTasksByUser(Long userId, Pageable pageable);
    
    /**
     * Get tasks by status
     */
    Page<TaskDTO> getTasksByStatus(Long userId, String status, Pageable pageable);
    
    /**
     * Get tasks by priority
     */
    Page<TaskDTO> getTasksByPriority(Long userId, String priority, Pageable pageable);
    
    /**
     * Get tasks by category
     */
    Page<TaskDTO> getTasksByCategory(Long userId, String category, Pageable pageable);
    
    /**
     * Search tasks by keyword
     */
    Page<TaskDTO> searchTasks(Long userId, String keyword, Pageable pageable);
    
    /**
     * Update a task
     */
    TaskDTO updateTask(Long taskId, Long userId, UpdateTaskRequest request);
    
    /**
     * Mark task as completed
     */
    TaskDTO markTaskAsCompleted(Long taskId, Long userId);
    
    /**
     * Delete a task
     */
    void deleteTask(Long taskId, Long userId);
    
    /**
     * Get overdue tasks
     */
    List<TaskDTO> getOverduetasks(Long userId);
    
    /**
     * Get dashboard statistics
     */
    DashboardDTO getDashboard(Long userId);
    
    /**
     * Get tasks due between dates
     */
    List<TaskDTO> getTasksDueInRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
