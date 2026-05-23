package com.taskmanagement.util;

import com.taskmanagement.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class for security context operations
 */
@Component
public class SecurityContextUtil {

    /**
     * Get current logged-in user ID
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user found");
        }
        return Long.parseLong(authentication.getName()); // This assumes the principal is the user ID
    }

    /**
     * Get current logged-in username
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user found");
        }
        return authentication.getName();
    }

    /**
     * Get current logged-in user's roles
     */
    public static java.util.Collection<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return java.util.Collections.emptyList();
        }
        return authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Check if current user is admin
     */
    public static boolean isCurrentUserAdmin() {
        return getCurrentUserRoles().contains("ROLE_ADMIN");
    }
}
