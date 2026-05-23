package com.taskmanagement.controller;

import com.taskmanagement.dto.response.ApiResponse;
import com.taskmanagement.dto.response.DashboardDTO;
import com.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dashboard REST Controller
 */
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard", description = "Dashboard and Statistics APIs")
@SecurityRequirement(name = "bearer_token")
public class DashboardController {

    private final TaskService taskService;

    /**
     * Get dashboard statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get dashboard statistics", description = "Retrieve user's dashboard with task statistics and productivity metrics")
    public ResponseEntity<ApiResponse<DashboardDTO>> getDashboardStatistics() {
        log.debug("Get dashboard statistics endpoint called");

        Long userId = getCurrentUserId();
        DashboardDTO dashboard = taskService.getDashboard(userId);

        ApiResponse<DashboardDTO> response = ApiResponse.success("Dashboard retrieved successfully", dashboard);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get current user ID from security context
     */
    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // In a real application, extract user ID from JWT token or database
        return 1L; // Placeholder - should be extracted from JWT
    }
}
