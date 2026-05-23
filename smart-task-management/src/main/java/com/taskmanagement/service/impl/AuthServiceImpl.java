package com.taskmanagement.service.impl;

import com.taskmanagement.dto.UserDTO;
import com.taskmanagement.dto.request.LoginRequest;
import com.taskmanagement.dto.request.RegisterRequest;
import com.taskmanagement.dto.response.LoginResponse;
import com.taskmanagement.entity.User;
import com.taskmanagement.exception.AuthenticationException;
import com.taskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.security.JwtUtil;
import com.taskmanagement.service.AuthService;
import com.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AuthService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Register a new user
     */
    @Override
    public UserDTO register(RegisterRequest request) {
        log.info("Registering user: {}", request.getUsername());
        return userService.register(request);
    }

    /**
     * Login user and return JWT token
     */
    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("Logging in user: {}", request.getUsername());

        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed for user: {} - invalid password", request.getUsername());
            throw new AuthenticationException("Invalid username or password");
        }

        // Check if user is active
        if (!user.getIsActive()) {
            log.warn("Login failed for user: {} - account inactive", request.getUsername());
            throw new AuthenticationException("User account is inactive");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        long expiresIn = jwtUtil.getExpirationTime(token);

        // Convert user to DTO
        UserDTO userDTO = convertToDTO(user);

        log.info("User logged in successfully: {}", request.getUsername());

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(userDTO)
                .expiresIn(expiresIn)
                .build();
    }

    /**
     * Validate JWT token
     */
    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    /**
     * Get username from token
     */
    @Override
    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }

    /**
     * Generate JWT token
     */
    @Override
    public String generateToken(String username) {
        return jwtUtil.generateToken(username);
    }

    /**
     * Convert User entity to UserDTO
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getValue())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .totalTasks(user.getTotalTasks())
                .completedTasks(user.getCompletedTasksCount())
                .pendingTasks(user.getPendingTasksCount())
                .build();
    }
}
