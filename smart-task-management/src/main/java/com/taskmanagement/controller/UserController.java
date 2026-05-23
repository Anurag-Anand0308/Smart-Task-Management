package com.taskmanagement.controller;

import com.taskmanagement.dto.UserDTO;
import com.taskmanagement.dto.response.ApiResponse;
import com.taskmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * User REST Controller
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management APIs")
@SecurityRequirement(name = "bearer_token")
public class UserController {

    private final UserService userService;

    /**
     * Get current user profile
     */
    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Get current logged-in user's profile")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUserProfile() {
        log.debug("Get current user profile endpoint called");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO user = userService.getUserByUsername(username);

        ApiResponse<UserDTO> response = ApiResponse.success("User profile retrieved successfully", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get user by id
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieve user information by user ID")
    @Parameter(name = "userId", description = "User ID")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long userId) {
        log.info("Get user by id endpoint called for userId: {}", userId);

        UserDTO user = userService.getUserById(userId);

        ApiResponse<UserDTO> response = ApiResponse.success("User retrieved successfully", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update user profile
     */
    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile", description = "Update user information")
    @Parameter(name = "userId", description = "User ID")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserDTO userDTO) {
        log.info("Update user endpoint called for userId: {}", userId);

        UserDTO updatedUser = userService.updateUser(userId, userDTO);

        ApiResponse<UserDTO> response = ApiResponse.success("User updated successfully", updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get user by username
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieve user information by username")
    @Parameter(name = "username", description = "Username")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUsername(@PathVariable String username) {
        log.info("Get user by username endpoint called for username: {}", username);

        UserDTO user = userService.getUserByUsername(username);

        ApiResponse<UserDTO> response = ApiResponse.success("User retrieved successfully", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
