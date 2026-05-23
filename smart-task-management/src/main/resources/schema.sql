-- Smart Task Management System Database Schema

-- Create Database
CREATE DATABASE IF NOT EXISTS smart_task_db;
USE smart_task_db;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_role CHECK (role IN ('USER', 'ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tasks Table
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description LONGTEXT,
    priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    due_date DATETIME,
    category VARCHAR(100),
    completed_at DATETIME NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_due_date (due_date),
    INDEX idx_category (category),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create indexes for better query performance
CREATE INDEX idx_user_created_at ON tasks(user_id, created_at);
CREATE INDEX idx_user_status ON tasks(user_id, status);
CREATE INDEX idx_user_priority_due_date ON tasks(user_id, priority, due_date);

-- Insert sample admin user (password: admin@123 - BCrypt hashed)
INSERT INTO users (username, email, password, role) 
VALUES ('admin', 'admin@taskmanagement.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gZvQOm', 'ADMIN')
ON DUPLICATE KEY UPDATE id=id;

-- Insert sample regular user (password: user@123 - BCrypt hashed)
INSERT INTO users (username, email, password, role) 
VALUES ('user1', 'user1@taskmanagement.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gZvQOm', 'USER')
ON DUPLICATE KEY UPDATE id=id;

-- Insert sample tasks
INSERT INTO tasks (title, description, priority, status, due_date, category, user_id) 
VALUES 
('Complete Project Documentation', 'Write comprehensive documentation for the task management system', 'HIGH', 'IN_PROGRESS', DATE_ADD(NOW(), INTERVAL 3 DAY), 'Work', 1),
('Review Code Changes', 'Review and approve pull requests from team members', 'MEDIUM', 'PENDING', DATE_ADD(NOW(), INTERVAL 5 DAY), 'Work', 1),
('Fix Bug in Authentication', 'Fix JWT token expiration issue in login flow', 'HIGH', 'PENDING', DATE_ADD(NOW(), INTERVAL 1 DAY), 'Bug', 1),
('Setup Database Backup', 'Configure automated daily database backups', 'LOW', 'PENDING', DATE_ADD(NOW(), INTERVAL 10 DAY), 'Infrastructure', 1),
('Update Dependencies', 'Update Spring Boot and other dependencies to latest versions', 'MEDIUM', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 2 DAY), 'Maintenance', 2);
