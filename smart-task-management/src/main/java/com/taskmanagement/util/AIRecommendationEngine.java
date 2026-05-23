package com.taskmanagement.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * AI Recommendation Engine for generating productivity suggestions
 */
@Component
@Slf4j
public class AIRecommendationEngine {

    /**
     * Generate recommendation based on task statistics
     */
    public String getRecommendation(long totalTasks, long completedTasks, long overdueTasks, long highPriorityTasks) {
        log.debug("Generating AI recommendation...");

        if (totalTasks == 0) {
            return "You have no tasks. Create some tasks to get started!";
        }

        double productivityPercentage = (completedTasks * 100.0 / totalTasks);

        // Logic for recommendations
        if (overdueTasks > 0) {
            return String.format("⚠️ You have %d overdue task(s). Focus on completing them immediately!", overdueTasks);
        }

        if (highPriorityTasks > 0) {
            return String.format("📌 You have %d high-priority task(s). Consider completing them first!", highPriorityTasks);
        }

        if (productivityPercentage >= 80) {
            return "🎉 Excellent work! You're maintaining a great productivity level. Keep it up!";
        } else if (productivityPercentage >= 50) {
            return "👍 Good progress! Try to complete more pending tasks to increase your productivity.";
        } else if (productivityPercentage >= 25) {
            return "💡 Let's pick up the pace! Focus on completing at least a few more tasks this week.";
        } else {
            return "🚀 Time to get focused! Break your tasks into smaller chunks and tackle them one by one.";
        }
    }

    /**
     * Suggest priority based on due date
     */
    public String suggestPriority(long daysUntilDue) {
        if (daysUntilDue <= 0) {
            return "HIGH"; // Overdue
        } else if (daysUntilDue <= 3) {
            return "HIGH"; // Due within 3 days
        } else if (daysUntilDue <= 7) {
            return "MEDIUM"; // Due within a week
        } else {
            return "LOW"; // Due later
        }
    }

    /**
     * Estimate task difficulty based on description length
     */
    public String estimateDifficulty(String description) {
        if (description == null || description.isEmpty()) {
            return "EASY";
        }

        int wordCount = description.split("\\s+").length;
        
        if (wordCount > 100) {
            return "HARD";
        } else if (wordCount > 50) {
            return "MEDIUM";
        } else {
            return "EASY";
        }
    }
}
