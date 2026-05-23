package com.taskmanagement.service;

import com.taskmanagement.dto.response.LoginResponse;
import com.taskmanagement.dto.request.LoginRequest;
import com.taskmanagement.dto.request.RegisterRequest;
import com.taskmanagement.dto.UserDTO;

/**
 * Service interface for authentication operations
 */
public interface AuthService {
    
    /**
     * Register a new user
     */
    UserDTO register(RegisterRequest request);
    
    /**
     * Login user and return JWT token
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * Validate JWT token
     */
    boolean validateToken(String token);
    
    /**
     * Get username from token
     */
    String getUsernameFromToken(String token);
    
    /**
     * Generate JWT token
     */
    String generateToken(String username);
}
