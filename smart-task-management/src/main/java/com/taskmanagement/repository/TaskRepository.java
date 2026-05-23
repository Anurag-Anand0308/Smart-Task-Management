package com.taskmanagement.repository;

import com.taskmanagement.entity.Task;
import com.taskmanagement.enums.Priority;
import com.taskmanagement.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Task entity
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    /**
     * Find all tasks by user id with pagination
     */
    Page<Task> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find all tasks by user id and status
     */
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
    
    /**
     * Find all tasks by user id and priority
     */
    List<Task> findByUserIdAndPriority(Long userId, Priority priority);
    
    /**
     * Find all tasks by user id and category
     */
    Page<Task> findByUserIdAndCategory(Long userId, String category, Pageable pageable);
    
    /**
     * Find tasks by user id and priority with pagination
     */
    Page<Task> findByUserIdAndPriority(Long userId, Priority priority, Pageable pageable);
    
    /**
     * Find tasks by user id and status with pagination
     */
    Page<Task> findByUserIdAndStatus(Long userId, TaskStatus status, Pageable pageable);
    
    /**
     * Count tasks by user id and status
     */
    long countByUserIdAndStatus(Long userId, TaskStatus status);
    
    /**
     * Count tasks by user id and priority
     */
    long countByUserIdAndPriority(Long userId, Priority priority);
    
    /**
     * Find overdue tasks for a user
     */
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.status != :status AND t.dueDate < :now")
    List<Task> findOverdueTasksByUserId(@Param("userId") Long userId, @Param("status") TaskStatus status, @Param("now") LocalDateTime now);
    
    /**
     * Find overdue tasks count
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId AND t.status != :status AND t.dueDate < :now")
    long countOverdueTasksByUserId(@Param("userId") Long userId, @Param("status") TaskStatus status, @Param("now") LocalDateTime now);
    
    /**
     * Search tasks by title or description
     */
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Task> searchTasks(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find tasks due between dates
     */
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.dueDate BETWEEN :startDate AND :endDate ORDER BY t.dueDate ASC")
    List<Task> findTasksBetweenDates(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Count all tasks by user
     */
    long countByUserId(Long userId);
}
