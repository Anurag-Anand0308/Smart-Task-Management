package com.taskmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user dashboard statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    
    private Long totalTasks;
    
    private Long completedTasks;
    
    private Long pendingTasks;
    
    private Long overdueTasks;
    
    private Double productivityPercentage;
    
    private Long highPriorityTasks;
    
    private Long mediumPriorityTasks;
    
    private Long lowPriorityTasks;
    
    private String recommendation;
}
