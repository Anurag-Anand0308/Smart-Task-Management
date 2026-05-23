package com.taskmanagement.controller;

import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.dto.request.CreateTaskRequest;
import com.taskmanagement.dto.request.UpdateTaskRequest;
import com.taskmanagement.dto.response.ApiResponse;
import com.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Task REST Controller
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tasks", description = "Task management APIs")
@SecurityRequirement(name = "bearer_token")
public class TaskController {

    private final TaskService taskService;

    /**
     * Create a new task
     */
    @PostMapping
    @Operation(summary = "Create new task", description = "Create a new task for the current user")
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        log.info("Create task endpoint called");

        Long userId = getCurrentUserId();
        TaskDTO task = taskService.createTask(userId, request);

        ApiResponse<TaskDTO> response = ApiResponse.created("Task created successfully", task);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get task by id
     */
    @GetMapping("/{taskId}")
    @Operation(summary = "Get task by ID", description = "Retrieve a specific task")
    @Parameter(name = "taskId", description = "Task ID")
    public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@PathVariable Long taskId) {
        log.info("Get task by id endpoint called for taskId: {}", taskId);

        Long userId = getCurrentUserId();
        TaskDTO task = taskService.getTaskById(taskId, userId);

        ApiResponse<TaskDTO> response = ApiResponse.success("Task retrieved successfully", task);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all tasks
     */
    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve all tasks with pagination and sorting")
    public ResponseEntity<ApiResponse<Page<TaskDTO>>> getAllTasks(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("Get all tasks endpoint called");

        Long userId = getCurrentUserId();
        Page<TaskDTO> tasks = taskService.getAllTasksByUser(userId, pageable);

        ApiResponse<Page<TaskDTO>> response = ApiResponse.success("Tasks retrieved successfully", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get tasks by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get tasks by status", description = "Retrieve tasks filtered by status")
    @Parameter(name = "status", description = "Task status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)")
    public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasksByStatus(
            @PathVariable String status,
            @PageableDefault(size = 10, sort = "dueDate", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Get tasks by status endpoint called for status: {}", status);

        Long userId = getCurrentUserId();
        Page<TaskDTO> tasks = taskService.getTasksByStatus(userId, status, pageable);

        ApiResponse<Page<TaskDTO>> response = ApiResponse.success("Tasks retrieved successfully", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get tasks by priority
     */
    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get tasks by priority", description = "Retrieve tasks filtered by priority")
    @Parameter(name = "priority", description = "Task priority (LOW, MEDIUM, HIGH)")
    public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasksByPriority(
            @PathVariable String priority,
            @PageableDefault(size = 10, sort = "dueDate", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Get tasks by priority endpoint called for priority: {}", priority);

        Long userId = getCurrentUserId();
        Page<TaskDTO> tasks = taskService.getTasksByPriority(userId, priority, pageable);

        ApiResponse<Page<TaskDTO>> response = ApiResponse.success("Tasks retrieved successfully", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get tasks by category
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Get tasks by category", description = "Retrieve tasks filtered by category")
    @Parameter(name = "category", description = "Task category")
    public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasksByCategory(
            @PathVariable String category,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Get tasks by category endpoint called for category: {}", category);

        Long userId = getCurrentUserId();
        Page<TaskDTO> tasks = taskService.getTasksByCategory(userId, category, pageable);

        ApiResponse<Page<TaskDTO>> response = ApiResponse.success("Tasks retrieved successfully", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Search tasks
     */
    @GetMapping("/search")
    @Operation(summary = "Search tasks", description = "Search tasks by keyword")
    @Parameter(name = "keyword", description = "Search keyword")
    public ResponseEntity<ApiResponse<Page<TaskDTO>>> searchTasks(
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Search tasks endpoint called with keyword: {}", keyword);

        Long userId = getCurrentUserId();
        Page<TaskDTO> tasks = taskService.searchTasks(userId, keyword, pageable);

        ApiResponse<Page<TaskDTO>> response = ApiResponse.success("Tasks found", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update task
     */
    @PutMapping("/{taskId}")
    @Operation(summary = "Update task", description = "Update an existing task")
    @Parameter(name = "taskId", description = "Task ID")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request) {
        log.info("Update task endpoint called for taskId: {}", taskId);

        Long userId = getCurrentUserId();
        TaskDTO task = taskService.updateTask(taskId, userId, request);

        ApiResponse<TaskDTO> response = ApiResponse.success("Task updated successfully", task);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Mark task as completed
     */
    @PutMapping("/{taskId}/complete")
    @Operation(summary = "Mark task as completed", description = "Mark a task as completed")
    @Parameter(name = "taskId", description = "Task ID")
    public ResponseEntity<ApiResponse<TaskDTO>> markTaskAsCompleted(@PathVariable Long taskId) {
        log.info("Mark task as completed endpoint called for taskId: {}", taskId);

        Long userId = getCurrentUserId();
        TaskDTO task = taskService.markTaskAsCompleted(taskId, userId);

        ApiResponse<TaskDTO> response = ApiResponse.success("Task marked as completed", task);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete task
     */
    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete task", description = "Delete a task")
    @Parameter(name = "taskId", description = "Task ID")
    public ResponseEntity<ApiResponse<Object>> deleteTask(@PathVariable Long taskId) {
        log.info("Delete task endpoint called for taskId: {}", taskId);

        Long userId = getCurrentUserId();
        taskService.deleteTask(taskId, userId);

        ApiResponse<Object> response = ApiResponse.success("Task deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Get overdue tasks
     */
    @GetMapping("/overdue")
    @Operation(summary = "Get overdue tasks", description = "Retrieve all overdue tasks")
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getOverdueTasks() {
        log.debug("Get overdue tasks endpoint called");

        Long userId = getCurrentUserId();
        List<TaskDTO> tasks = taskService.getOverduetasks(userId);

        ApiResponse<List<TaskDTO>> response = ApiResponse.success("Overdue tasks retrieved", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get tasks due in range
     */
    @GetMapping("/due-range")
    @Operation(summary = "Get tasks due in range", description = "Retrieve tasks due between two dates")
    @Parameter(name = "startDate", description = "Start date (format: yyyy-MM-dd'T'HH:mm:ss)")
    @Parameter(name = "endDate", description = "End date (format: yyyy-MM-dd'T'HH:mm:ss)")
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getTasksDueInRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        log.info("Get tasks due in range endpoint called");

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        Long userId = getCurrentUserId();
        List<TaskDTO> tasks = taskService.getTasksDueInRange(userId, start, end);

        ApiResponse<List<TaskDTO>> response = ApiResponse.success("Tasks due in range retrieved", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get current user ID from security context
     */
    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // In a real application, you might want to fetch the user ID from the database or JWT token
        // For now, we'll return a placeholder. This should be extracted from the JWT token
        return 1L; // This should be replaced with actual user ID extraction
    }
}
