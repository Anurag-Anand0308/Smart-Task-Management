package com.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for Smart Task Management System
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.taskmanagement.repository")
@EnableTransactionManagement
@ComponentScan(basePackages = "com.taskmanagement")
public class SmartTaskManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTaskManagementApplication.class, args);
    }
}
