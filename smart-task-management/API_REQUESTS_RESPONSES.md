# API Requests & Responses Documentation

## Complete API Request/Response Examples

This document provides detailed examples of all API requests and their corresponding responses.

---

## Authentication APIs

### 1. Register User

**Request:**
```http
POST http://localhost:8080/api/auth/register
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
    "id": 2,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "USER",
    "isActive": true,
    "createdAt": "2026-05-23T10:30:00",
    "updatedAt": "2026-05-23T10:30:00",
    "totalTasks": 0,
    "completedTasks": 0,
    "pendingTasks": 0
  },
  "statusCode": 201,
  "timestamp": "2026-05-23T10:30:00"
}
```

### 2. Login User

**Request:**
```http
POST http://localhost:8080/api/auth/login
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
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTY4NzUwMTQwMCwiZXhwIjoxNjg3NTg3ODAwfQ.signature",
    "tokenType": "Bearer",
    "user": {
      "id": 2,
      "username": "john_doe",
      "email": "john@example.com",
      "role": "USER",
      "isActive": true,
      "createdAt": "2026-05-23T10:30:00",
      "updatedAt": "2026-05-23T10:30:00",
      "totalTasks": 0,
      "completedTasks": 0,
      "pendingTasks": 0
    },
    "expiresIn": 1687587800000
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T10:30:01"
}
```

### 3. Health Check

**Request:**
```http
GET http://localhost:8080/api/auth/health
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Authentication service is running",
  "statusCode": 200,
  "timestamp": "2026-05-23T10:30:02"
}
```

---

## Task APIs

### 4. Create Task

**Request:**
```http
POST http://localhost:8080/api/tasks
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "title": "Complete project documentation",
  "description": "Write comprehensive API documentation for all endpoints with examples",
  "priority": "HIGH",
  "dueDate": "2026-06-01T18:00:00",
  "category": "Work"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": 10,
    "title": "Complete project documentation",
    "description": "Write comprehensive API documentation for all endpoints with examples",
    "priority": "HIGH",
    "status": "PENDING",
    "dueDate": "2026-06-01T18:00:00",
    "category": "Work",
    "isOverdue": false,
    "isDueSoon": false,
    "completedAt": null,
    "createdAt": "2026-05-23T10:35:00",
    "updatedAt": "2026-05-23T10:35:00",
    "userId": 2,
    "username": "john_doe"
  },
  "statusCode": 201,
  "timestamp": "2026-05-23T10:35:00"
}
```

### 5. Get All Tasks (with Pagination)

**Request:**
```http
GET http://localhost:8080/api/tasks?page=0&size=10&sort=createdAt,desc
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Tasks retrieved successfully",
  "data": {
    "content": [
      {
        "id": 10,
        "title": "Complete project documentation",
        "description": "Write comprehensive API documentation for all endpoints with examples",
        "priority": "HIGH",
        "status": "PENDING",
        "dueDate": "2026-06-01T18:00:00",
        "category": "Work",
        "isOverdue": false,
        "isDueSoon": false,
        "completedAt": null,
        "createdAt": "2026-05-23T10:35:00",
        "updatedAt": "2026-05-23T10:35:00",
        "userId": 2,
        "username": "john_doe"
      },
      {
        "id": 9,
        "title": "Review code changes",
        "description": "Review and approve pull requests",
        "priority": "MEDIUM",
        "status": "PENDING",
        "dueDate": "2026-05-28T15:00:00",
        "category": "Work",
        "isOverdue": false,
        "isDueSoon": true,
        "completedAt": null,
        "createdAt": "2026-05-23T10:30:00",
        "updatedAt": "2026-05-23T10:30:00",
        "userId": 2,
        "username": "john_doe"
      }
    ],
    "pageable": {
      "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
      },
      "offset": 0,
      "pageNumber": 0,
      "pageSize": 10,
      "paged": true,
      "unpaged": false
    },
    "last": true,
    "totalElements": 2,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T10:40:00"
}
```

### 6. Get Task by ID

**Request:**
```http
GET http://localhost:8080/api/tasks/10
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Task retrieved successfully",
  "data": {
    "id": 10,
    "title": "Complete project documentation",
    "description": "Write comprehensive API documentation for all endpoints with examples",
    "priority": "HIGH",
    "status": "PENDING",
    "dueDate": "2026-06-01T18:00:00",
    "category": "Work",
    "isOverdue": false,
    "isDueSoon": false,
    "completedAt": null,
    "createdAt": "2026-05-23T10:35:00",
    "updatedAt": "2026-05-23T10:35:00",
    "userId": 2,
    "username": "john_doe"
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T10:42:00"
}
```

### 7. Get Tasks by Status

**Request:**
```http
GET http://localhost:8080/api/tasks/status/PENDING?page=0&size=10
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Tasks retrieved successfully",
  "data": {
    "content": [
      {
        "id": 10,
        "title": "Complete project documentation",
        "description": "Write comprehensive API documentation for all endpoints with examples",
        "priority": "HIGH",
        "status": "PENDING",
        "dueDate": "2026-06-01T18:00:00",
        "category": "Work",
        "isOverdue": false,
        "isDueSoon": false,
        "completedAt": null,
        "createdAt": "2026-05-23T10:35:00",
        "updatedAt": "2026-05-23T10:35:00",
        "userId": 2,
        "username": "john_doe"
      }
    ],
    "pageable": {...},
    "totalElements": 1,
    "totalPages": 1
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T10:45:00"
}
```

### 8. Get Tasks by Priority

**Request:**
```http
GET http://localhost:8080/api/tasks/priority/HIGH?page=0&size=10
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Tasks retrieved successfully",
  "data": {
    "content": [
      {
        "id": 10,
        "title": "Complete project documentation",
        "priority": "HIGH",
        "status": "PENDING",
        "dueDate": "2026-06-01T18:00:00",
        "category": "Work",
        "isOverdue": false,
        "isDueSoon": false,
        "userId": 2
      }
    ],
    "totalElements": 1,
    "totalPages": 1
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T10:47:00"
}
```

### 9. Search Tasks

**Request:**
```http
GET http://localhost:8080/api/tasks/search?keyword=documentation&page=0&size=10
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Tasks found",
  "data": {
    "content": [
      {
        "id": 10,
        "title": "Complete project documentation",
        "description": "Write comprehensive API documentation for all endpoints with examples",
        "priority": "HIGH",
        "status": "PENDING",
        "dueDate": "2026-06-01T18:00:00",
        "category": "Work",
        "userId": 2
      }
    ],
    "totalElements": 1,
    "totalPages": 1
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T10:50:00"
}
```

### 10. Update Task

**Request:**
```http
PUT http://localhost:8080/api/tasks/10
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "title": "Complete API documentation",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "dueDate": "2026-05-31T18:00:00"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Task updated successfully",
  "data": {
    "id": 10,
    "title": "Complete API documentation",
    "description": "Write comprehensive API documentation for all endpoints with examples",
    "priority": "MEDIUM",
    "status": "IN_PROGRESS",
    "dueDate": "2026-05-31T18:00:00",
    "category": "Work",
    "isOverdue": false,
    "isDueSoon": true,
    "completedAt": null,
    "createdAt": "2026-05-23T10:35:00",
    "updatedAt": "2026-05-23T10:52:00",
    "userId": 2,
    "username": "john_doe"
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T10:52:00"
}
```

### 11. Mark Task as Completed

**Request:**
```http
PUT http://localhost:8080/api/tasks/10/complete
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Task marked as completed",
  "data": {
    "id": 10,
    "title": "Complete API documentation",
    "description": "Write comprehensive API documentation for all endpoints with examples",
    "priority": "MEDIUM",
    "status": "COMPLETED",
    "dueDate": "2026-05-31T18:00:00",
    "category": "Work",
    "isOverdue": false,
    "isDueSoon": false,
    "completedAt": "2026-05-23T10:55:00",
    "createdAt": "2026-05-23T10:35:00",
    "updatedAt": "2026-05-23T10:55:00",
    "userId": 2,
    "username": "john_doe"
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T10:55:00"
}
```

### 12. Delete Task

**Request:**
```http
DELETE http://localhost:8080/api/tasks/10
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (204 No Content):**
```json
{
  "success": true,
  "message": "Task deleted successfully",
  "statusCode": 204,
  "timestamp": "2026-05-23T10:57:00"
}
```

### 13. Get Overdue Tasks

**Request:**
```http
GET http://localhost:8080/api/tasks/overdue
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Overdue tasks retrieved",
  "data": [
    {
      "id": 5,
      "title": "Fix critical bug",
      "priority": "HIGH",
      "status": "PENDING",
      "dueDate": "2026-05-15T10:00:00",
      "category": "Bug",
      "isOverdue": true,
      "isDueSoon": false,
      "userId": 2,
      "username": "john_doe"
    }
  ],
  "statusCode": 200,
  "timestamp": "2026-05-23T11:00:00"
}
```

---

## Dashboard API

### 14. Get Dashboard Statistics

**Request:**
```http
GET http://localhost:8080/api/dashboard/statistics
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
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
  "statusCode": 200,
  "timestamp": "2026-05-23T11:05:00"
}
```

---

## User APIs

### 15. Get Current User Profile

**Request:**
```http
GET http://localhost:8080/api/users/profile
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User profile retrieved successfully",
  "data": {
    "id": 2,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "USER",
    "isActive": true,
    "createdAt": "2026-05-23T10:30:00",
    "updatedAt": "2026-05-23T10:30:00",
    "totalTasks": 25,
    "completedTasks": 15,
    "pendingTasks": 10
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T11:08:00"
}
```

### 16. Get User by ID

**Request:**
```http
GET http://localhost:8080/api/users/2
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": 2,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "USER",
    "isActive": true,
    "createdAt": "2026-05-23T10:30:00",
    "updatedAt": "2026-05-23T10:30:00",
    "totalTasks": 25,
    "completedTasks": 15,
    "pendingTasks": 10
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T11:10:00"
}
```

### 17. Update User Profile

**Request:**
```http
PUT http://localhost:8080/api/users/2
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "username": "john_doe_updated",
  "email": "john_updated@example.com"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 2,
    "username": "john_doe_updated",
    "email": "john_updated@example.com",
    "role": "USER",
    "isActive": true,
    "createdAt": "2026-05-23T10:30:00",
    "updatedAt": "2026-05-23T11:12:00",
    "totalTasks": 25,
    "completedTasks": 15,
    "pendingTasks": 10
  },
  "statusCode": 200,
  "timestamp": "2026-05-23T11:12:00"
}
```

---

## Error Response Examples

### 401 Unauthorized (Missing Token)

**Response:**
```json
{
  "success": false,
  "message": "Unauthorized access",
  "statusCode": 401,
  "timestamp": "2026-05-23T11:15:00"
}
```

### 400 Bad Request (Validation Error)

**Response:**
```json
{
  "success": false,
  "message": "Validation failed",
  "error": "title: Task title is required; priority: Invalid priority: URGENT; ",
  "statusCode": 400,
  "timestamp": "2026-05-23T11:17:00"
}
```

### 404 Not Found

**Response:**
```json
{
  "success": false,
  "message": "Task not found with id: '999'",
  "error": "RESOURCE_NOT_FOUND",
  "statusCode": 404,
  "timestamp": "2026-05-23T11:20:00"
}
```

### 500 Internal Server Error

**Response:**
```json
{
  "success": false,
  "message": "An unexpected error occurred. Please try again later.",
  "error": "NullPointerException",
  "statusCode": 500,
  "timestamp": "2026-05-23T11:22:00"
}
```

---

## HTTP Status Codes Used

| Status Code | Description |
|-------------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Request successful, no content to return |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Authentication required |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

---

## Request/Response Headers

### Request Headers (for authenticated requests)
```
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

### Response Headers
```
Content-Type: application/json
```

---

**Note**: Replace `{jwt_token}` with actual JWT token obtained from login endpoint.
