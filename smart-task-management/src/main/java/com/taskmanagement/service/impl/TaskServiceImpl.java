package com.taskmanagement.service.impl;

import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.dto.response.DashboardDTO;
import com.taskmanagement.dto.request.CreateTaskRequest;
import com.taskmanagement.dto.request.UpdateTaskRequest;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.enums.Priority;
import com.taskmanagement.enums.TaskStatus;
import com.taskmanagement.exception.BadRequestException;
import com.taskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.util.AIRecommendationEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of TaskService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AIRecommendationEngine aiEngine;

    /**
     * Create a new task
     */
    @Override
    @Transactional
    public TaskDTO createTask(Long userId, CreateTaskRequest request) {
        log.info("Creating new task for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Set default priority if not provided
        Priority priority = Priority.MEDIUM;
        if (request.getPriority() != null) {
            try {
                priority = Priority.fromValue(request.getPriority());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid priority: " + request.getPriority());
            }
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(priority)
                .status(TaskStatus.PENDING)
                .dueDate(request.getDueDate())
                .category(request.getCategory())
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());

        return convertToDTO(savedTask);
    }

    /**
     * Get task by id
     */
    @Override
    public TaskDTO getTaskById(Long taskId, Long userId) {
        log.debug("Fetching task with id: {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        // Verify that the task belongs to the user
        if (!task.getUser().getId().equals(userId)) {
            throw new BadRequestException("Task does not belong to this user");
        }

        return convertToDTO(task);
    }

    /**
     * Get all tasks for a user with pagination
     */
    @Override
    public Page<TaskDTO> getAllTasksByUser(Long userId, Pageable pageable) {
        log.debug("Fetching all tasks for user: {}", userId);

        Page<Task> tasks = taskRepository.findByUserId(userId, pageable);
        return tasks.map(this::convertToDTO);
    }

    /**
     * Get tasks by status
     */
    @Override
    public Page<TaskDTO> getTasksByStatus(Long userId, String status, Pageable pageable) {
        log.debug("Fetching tasks for user: {} with status: {}", userId, status);

        try {
            TaskStatus taskStatus = TaskStatus.fromValue(status);
            Page<Task> tasks = taskRepository.findByUserIdAndStatus(userId, taskStatus, pageable);
            return tasks.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status);
        }
    }

    /**
     * Get tasks by priority
     */
    @Override
    public Page<TaskDTO> getTasksByPriority(Long userId, String priority, Pageable pageable) {
        log.debug("Fetching tasks for user: {} with priority: {}", userId, priority);

        try {
            Priority taskPriority = Priority.fromValue(priority);
            Page<Task> tasks = taskRepository.findByUserIdAndPriority(userId, taskPriority, pageable);
            return tasks.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid priority: " + priority);
        }
    }

    /**
     * Get tasks by category
     */
    @Override
    public Page<TaskDTO> getTasksByCategory(Long userId, String category, Pageable pageable) {
        log.debug("Fetching tasks for user: {} with category: {}", userId, category);

        Page<Task> tasks = taskRepository.findByUserIdAndCategory(userId, category, pageable);
        return tasks.map(this::convertToDTO);
    }

    /**
     * Search tasks by keyword
     */
    @Override
    public Page<TaskDTO> searchTasks(Long userId, String keyword, Pageable pageable) {
        log.debug("Searching tasks for user: {} with keyword: {}", userId, keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }

        Page<Task> tasks = taskRepository.searchTasks(userId, keyword, pageable);
        return tasks.map(this::convertToDTO);
    }

    /**
     * Update a task
     */
    @Override
    @Transactional
    public TaskDTO updateTask(Long taskId, Long userId, UpdateTaskRequest request) {
        log.info("Updating task with id: {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        // Verify ownership
        if (!task.getUser().getId().equals(userId)) {
            throw new BadRequestException("Task does not belong to this user");
        }

        // Update fields if provided
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getPriority() != null) {
            try {
                task.setPriority(Priority.fromValue(request.getPriority()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid priority: " + request.getPriority());
            }
        }

        if (request.getStatus() != null) {
            try {
                TaskStatus status = TaskStatus.fromValue(request.getStatus());
                task.setStatus(status);
                
                // If marking as completed, set completion time
                if (status.equals(TaskStatus.COMPLETED) && task.getCompletedAt() == null) {
                    task.setCompletedAt(LocalDateTime.now());
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid status: " + request.getStatus());
            }
        }

        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        if (request.getCategory() != null) {
            task.setCategory(request.getCategory());
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully with id: {}", taskId);

        return convertToDTO(updatedTask);
    }

    /**
     * Mark task as completed
     */
    @Override
    @Transactional
    public TaskDTO markTaskAsCompleted(Long taskId, Long userId) {
        log.info("Marking task as completed with id: {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        // Verify ownership
        if (!task.getUser().getId().equals(userId)) {
            throw new BadRequestException("Task does not belong to this user");
        }

        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());

        Task completedTask = taskRepository.save(task);
        log.info("Task marked as completed with id: {}", taskId);

        return convertToDTO(completedTask);
    }

    /**
     * Delete a task
     */
    @Override
    @Transactional
    public void deleteTask(Long taskId, Long userId) {
        log.info("Deleting task with id: {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        // Verify ownership
        if (!task.getUser().getId().equals(userId)) {
            throw new BadRequestException("Task does not belong to this user");
        }

        taskRepository.delete(task);
        log.info("Task deleted successfully with id: {}", taskId);
    }

    /**
     * Get overdue tasks
     */
    @Override
    public List<TaskDTO> getOverduetasks(Long userId) {
        log.debug("Fetching overdue tasks for user: {}", userId);

        List<Task> overdueTasks = taskRepository.findOverdueTasksByUserId(userId, TaskStatus.COMPLETED, LocalDateTime.now());
        return overdueTasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get dashboard statistics
     */
    @Override
    public DashboardDTO getDashboard(Long userId) {
        log.debug("Generating dashboard for user: {}", userId);

        long totalTasks = taskRepository.countByUserId(userId);
        long completedTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.COMPLETED);
        long pendingTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.PENDING);
        long overdueTasks = taskRepository.countOverdueTasksByUserId(userId, TaskStatus.COMPLETED, LocalDateTime.now());
        long highPriorityTasks = taskRepository.countByUserIdAndPriority(userId, Priority.HIGH);
        long mediumPriorityTasks = taskRepository.countByUserIdAndPriority(userId, Priority.MEDIUM);
        long lowPriorityTasks = taskRepository.countByUserIdAndPriority(userId, Priority.LOW);

        // Calculate productivity percentage
        double productivityPercentage = totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0.0;

        // Get AI recommendation
        String recommendation = aiEngine.getRecommendation(
                totalTasks,
                completedTasks,
                overdueTasks,
                highPriorityTasks
        );

        return DashboardDTO.builder()
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .overdueTasks(overdueTasks)
                .productivityPercentage(Math.round(productivityPercentage * 100.0) / 100.0)
                .highPriorityTasks(highPriorityTasks)
                .mediumPriorityTasks(mediumPriorityTasks)
                .lowPriorityTasks(lowPriorityTasks)
                .recommendation(recommendation)
                .build();
    }

    /**
     * Get tasks due between dates
     */
    @Override
    public List<TaskDTO> getTasksDueInRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching tasks for user: {} due between {} and {}", userId, startDate, endDate);

        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date must be before end date");
        }

        List<Task> tasks = taskRepository.findTasksBetweenDates(userId, startDate, endDate);
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Task entity to TaskDTO
     */
    private TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().getValue())
                .status(task.getStatus().getValue())
                .dueDate(task.getDueDate())
                .category(task.getCategory())
                .isOverdue(task.isOverdue())
                .isDueSoon(task.isDueSoon())
                .completedAt(task.getCompletedAt())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .userId(task.getUser().getId())
                .username(task.getUser().getUsername())
                .build();
    }
}
