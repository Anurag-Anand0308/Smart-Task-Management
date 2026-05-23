package com.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.taskmanagement.enums.Priority;
import com.taskmanagement.enums.TaskStatus;

import java.time.LocalDateTime;

/**
 * Task Entity - Represents a task in the system
 */
@Entity
@Table(name = "tasks", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_priority", columnList = "priority"),
        @Index(name = "idx_due_date", columnList = "due_date"),
        @Index(name = "idx_category", columnList = "category")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(length = 100)
    private String category;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Many tasks belong to one user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Check if task is overdue
     */
    public boolean isOverdue() {
        if (dueDate == null || status.equals(TaskStatus.COMPLETED)) {
            return false;
        }
        return LocalDateTime.now().isAfter(dueDate);
    }

    /**
     * Check if task is due soon (within 24 hours)
     */
    public boolean isDueSoon() {
        if (dueDate == null || status.equals(TaskStatus.COMPLETED)) {
            return false;
        }
        LocalDateTime tomorrow = LocalDateTime.now().plusHours(24);
        return dueDate.isBefore(tomorrow) && dueDate.isAfter(LocalDateTime.now());
    }

    /**
     * Get priority score for sorting (HIGH=3, MEDIUM=2, LOW=1)
     */
    public int getPriorityScore() {
        return switch (priority) {
            case HIGH -> 3;
            case MEDIUM -> 2;
            case LOW -> 1;
        };
    }

    /**
     * Get urgency score based on due date
     */
    public int getUrgencyScore() {
        if (dueDate == null) {
            return 0;
        }
        long daysUntilDue = java.time.temporal.ChronoUnit.DAYS.between(LocalDateTime.now(), dueDate);
        if (daysUntilDue < 0) {
            return 100; // Overdue - highest urgency
        } else if (daysUntilDue == 0) {
            return 50;  // Due today
        } else if (daysUntilDue <= 3) {
            return 30;  // Due within 3 days
        } else if (daysUntilDue <= 7) {
            return 20;  // Due within a week
        }
        return 10;  // Due later
    }
}
