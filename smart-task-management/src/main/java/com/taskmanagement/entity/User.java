package com.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.taskmanagement.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User Entity - Represents a user in the system
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * One user can have many tasks
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    /**
     * Get total number of tasks for this user
     */
    public int getTotalTasks() {
        return tasks == null ? 0 : tasks.size();
    }

    /**
     * Get number of completed tasks
     */
    public int getCompletedTasksCount() {
        return tasks == null ? 0 : (int) tasks.stream()
                .filter(task -> task.getStatus().equals(com.taskmanagement.enums.TaskStatus.COMPLETED))
                .count();
    }

    /**
     * Get number of pending tasks
     */
    public int getPendingTasksCount() {
        return tasks == null ? 0 : (int) tasks.stream()
                .filter(task -> !task.getStatus().equals(com.taskmanagement.enums.TaskStatus.COMPLETED))
                .count();
    }
}
