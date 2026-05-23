package com.taskmanagement.service;

import com.taskmanagement.dto.UserDTO;
import com.taskmanagement.dto.request.RegisterRequest;

/**
 * Service interface for user operations
 */
public interface UserService {
    
    /**
     * Register a new user
     */
    UserDTO register(RegisterRequest request);
    
    /**
     * Get user by id
     */
    UserDTO getUserById(Long userId);
    
    /**
     * Get user by username
     */
    UserDTO getUserByUsername(String username);
    
    /**
     * Update user profile
     */
    UserDTO updateUser(Long userId, UserDTO userDTO);
    
    /**
     * Check if username exists
     */
    boolean isUsernameExists(String username);
    
    /**
     * Check if email exists
     */
    boolean isEmailExists(String email);
}
