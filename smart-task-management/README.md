# Smart Task Management System

## Overview

An AI-Powered Smart Task Management System built with **Spring Boot 3.x** and **Java 17**. This is a production-ready backend application that provides task management with advanced features including task prioritization, deadlines, status tracking, filtering, and AI-based productivity suggestions.

## Tech Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security** with JWT Authentication
- **Spring Data JPA** with Hibernate
- **MySQL** Database
- **Maven** Build Tool
- **Lombok** for boilerplate reduction
- **Swagger/OpenAPI** for API documentation
- **JUnit 5** and **Mockito** for testing
- **SLF4J** for logging

## Project Structure

```
smart-task-management/
├── src/
│   ├── main/
│   │   ├── java/com/taskmanagement/
│   │   │   ├── controller/           # REST API Controllers
│   │   │   ├── service/              # Business Logic
│   │   │   ├── repository/           # Data Access Layer
│   │   │   ├── entity/               # JPA Entities
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   ├── exception/            # Custom Exceptions
│   │   │   ├── security/             # JWT & Security
│   │   │   ├── config/               # Application Configuration
│   │   │   ├── enums/                # Enumerations
│   │   │   ├── util/                 # Utility Classes
│   │   │   └── SmartTaskManagementApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── schema.sql
│   └── test/
│       └── java/com/taskmanagement/  # Unit Tests
├── pom.xml
└── Smart-Task-Management-API.postman_collection.json
```

## Prerequisites

- **Java 17+** installed
- **Maven 3.6+** installed
- **MySQL 8.0+** installed and running
- **Postman** for API testing (optional)

## Installation & Setup

### Step 1: Clone or Download the Project

```bash
cd d:\project\java project\smart-task-management
```

### Step 2: Configure Database

1. Start your MySQL server
2. Create a new database:

```sql
CREATE DATABASE smart_task_db;
```

3. Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_task_db
spring.datasource.username=root
spring.datasource.password=your_mysql_password
jwt.secret=your_super_secret_jwt_key_must_be_long_enough_for_hs512_algorithm
```

### Step 3: Build the Project

```bash
mvn clean build
```

### Step 4: Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## Features

### 1. User Authentication & Authorization
- User registration with validation
- Login with JWT token generation
- Role-based access control (USER, ADMIN)
- Password encryption using BCrypt
- Stateless authentication

### 2. Task Management
- **Create Task**: Add new tasks with title, description, priority, and due date
- **Read Task**: Retrieve tasks with pagination and sorting
- **Update Task**: Modify task details
- **Delete Task**: Remove tasks
- **Mark Completed**: Mark tasks as complete with timestamp
- **Status Tracking**: Track task status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)

### 3. Task Filtering & Searching
- Filter by priority (LOW, MEDIUM, HIGH)
- Filter by status
- Filter by category
- Filter by due date range
- Full-text search by title and description
- Pagination support for all list endpoints

### 4. Dashboard & Analytics
- Total tasks count
- Completed tasks count
- Pending tasks count
- Overdue tasks count
- Productivity percentage calculation
- AI-powered recommendations

### 5. AI-Based Features
- Automatic priority suggestion based on due date
- Overdue task detection
- Productivity score calculation
- Smart recommendations based on task statistics
- Difficulty estimation

### 6. Security
- JWT-based authentication
- Authorization filters
- Input validation
- SQL injection prevention via JPA
- CORS support ready

## API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePassword@123",
  "confirmPassword": "SecurePassword@123"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "USER",
    "isActive": true,
    "createdAt": "2026-05-23T10:30:00",
    "updatedAt": "2026-05-23T10:30:00"
  },
  "statusCode": 201
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePassword@123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "user": {
      "id": 1,
      "username": "john_doe",
      "email": "john@example.com",
      "role": "USER"
    },
    "expiresIn": 1632567800000
  },
  "statusCode": 200
}
```

### Task Endpoints

#### Create Task
```http
POST /api/tasks
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "title": "Complete project documentation",
  "description": "Write comprehensive API documentation",
  "priority": "HIGH",
  "dueDate": "2026-06-01T18:00:00",
  "category": "Work"
}
```

#### Get All Tasks
```http
GET /api/tasks?page=0&size=10&sort=createdAt,desc
Authorization: Bearer {jwt_token}
```

#### Get Tasks by Priority
```http
GET /api/tasks/priority/HIGH?page=0&size=10
Authorization: Bearer {jwt_token}
```

#### Get Tasks by Status
```http
GET /api/tasks/status/PENDING?page=0&size=10
Authorization: Bearer {jwt_token}
```

#### Search Tasks
```http
GET /api/tasks/search?keyword=documentation&page=0&size=10
Authorization: Bearer {jwt_token}
```

#### Update Task
```http
PUT /api/tasks/{taskId}
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "title": "Updated title",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM"
}
```

#### Mark Task as Completed
```http
PUT /api/tasks/{taskId}/complete
Authorization: Bearer {jwt_token}
```

#### Delete Task
```http
DELETE /api/tasks/{taskId}
Authorization: Bearer {jwt_token}
```

#### Get Overdue Tasks
```http
GET /api/tasks/overdue
Authorization: Bearer {jwt_token}
```

### Dashboard Endpoint

#### Get Dashboard Statistics
```http
GET /api/dashboard/statistics
Authorization: Bearer {jwt_token}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Dashboard retrieved successfully",
  "data": {
    "totalTasks": 25,
    "completedTasks": 15,
    "pendingTasks": 10,
    "overdueTasks": 2,
    "productivityPercentage": 60.0,
    "highPriorityTasks": 5,
    "mediumPriorityTasks": 10,
    "lowPriorityTasks": 10,
    "recommendation": "Good progress! Try to complete more pending tasks to increase your productivity."
  },
  "statusCode": 200
}
```

### User Endpoints

#### Get Current User Profile
```http
GET /api/users/profile
Authorization: Bearer {jwt_token}
```

#### Get User by ID
```http
GET /api/users/{userId}
Authorization: Bearer {jwt_token}
```

#### Update User Profile
```http
PUT /api/users/{userId}
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "username": "new_username",
  "email": "new_email@example.com"
}
```

## Swagger/OpenAPI Documentation

Once the application is running, access the API documentation at:

```
http://localhost:8080/api/swagger-ui.html
```

This provides an interactive interface to test all API endpoints.

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Tasks Table
```sql
CREATE TABLE tasks (
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
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## Configuration

### JWT Configuration (application.properties)

```properties
jwt.secret=your_super_secret_jwt_key_must_be_long_enough_for_hs512_algorithm
jwt.expiration=86400000  # 24 hours in milliseconds
```

### Database Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_task_db
spring.datasource.username=root
spring.datasource.password=root@123
spring.jpa.hibernate.ddl-auto=update
```

## Security

- **JWT Authentication**: Token-based stateless authentication
- **Password Encoding**: BCrypt algorithm for secure password storage
- **Authorization Filters**: Role-based access control
- **Input Validation**: Request validation using annotations
- **SQL Injection Prevention**: Parameterized queries via JPA

## Logging

Application logs are stored in `logs/application.log`:

```properties
logging.level.root=INFO
logging.level.com.taskmanagement=DEBUG
logging.file.name=logs/application.log
logging.file.max-size=10MB
```

## Testing

### Run Unit Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=UserServiceTest
```

### Run with Coverage

```bash
mvn test jacoco:report
```

## Postman Collection

Import the provided Postman collection to test all endpoints:

1. Open Postman
2. Click "Import"
3. Select `Smart-Task-Management-API.postman_collection.json`
4. Configure the `base_url` variable to `http://localhost:8080/api`
5. Register and login to get JWT token (stored automatically in `jwt_token` variable)

## Sample Data

Default sample users are created during startup:

**Admin User:**
- Username: `admin`
- Password: `admin@123`
- Email: `admin@taskmanagement.com`

**Regular User:**
- Username: `user1`
- Password: `user@123`
- Email: `user1@taskmanagement.com`

## Troubleshooting

### Database Connection Error
```
Error: "Unable to connect to database"
Solution: Check MySQL is running, credentials in application.properties are correct
```

### JWT Token Expired
```
Error: "JWT token is expired"
Solution: Login again to get a new token
```

### Port Already in Use
```
Error: "Port 8080 is already in use"
Solution: Change port in application.properties: server.port=8081
```

## Performance Optimization

- **Connection Pooling**: HikariCP configured for optimal connection management
- **Query Optimization**: Indexed database columns for frequently queried fields
- **Lazy Loading**: Hibernate lazy loading for related entities
- **Caching**: Spring Cache ready for implementation
- **Pagination**: All list endpoints support pagination

## Future Enhancements

- [ ] WebSocket support for real-time notifications
- [ ] File attachment support for tasks
- [ ] Task sharing and collaboration
- [ ] Advanced analytics and reporting
- [ ] Mobile app support
- [ ] Calendar integration
- [ ] Email notifications
- [ ] Task templates
- [ ] Recurring tasks
- [ ] Team collaboration features

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Support

For issues, questions, or suggestions, please create an issue in the repository.

## Author

Smart Task Management Development Team

## Acknowledgments

- Spring Boot Community
- OpenAPI/Swagger
- JWT Community
- JUnit and Mockito Teams

---

**Last Updated**: May 23, 2026
**Version**: 1.0.0
