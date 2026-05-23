# Smart Task Management System - Architecture Documentation

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                              │
│  (Web Browser, Mobile App, Postman, cURL, etc.)                │
└──────────────────────────┬──────────────────────────────────────┘
                           │ HTTP/HTTPS Requests
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                            │
│  Controllers (REST API Endpoints)                               │
│  ├── AuthController                                              │
│  ├── UserController                                              │
│  ├── TaskController                                              │
│  └── DashboardController                                         │
└──────────────────────────┬──────────────────────────────────────┘
                           │ Request Dispatch
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SECURITY LAYER                                │
│  JWT Authentication Filter & Spring Security                    │
│  ├── JwtAuthenticationFilter                                     │
│  ├── CustomUserDetailsService                                    │
│  └── SecurityConfiguration                                       │
└──────────────────────────┬──────────────────────────────────────┘
                           │ Authenticated Request
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                  BUSINESS LOGIC LAYER                            │
│  Service Layer (DTOs In → DTOs Out)                             │
│  ├── UserService/UserServiceImpl                                 │
│  ├── AuthService/AuthServiceImpl                                 │
│  ├── TaskService/TaskServiceImpl                                 │
│  └── AIRecommendationEngine                                      │
└──────────────────────────┬──────────────────────────────────────┘
                           │ Entity Operations
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                   DATA ACCESS LAYER                              │
│  Repository Layer (JPA)                                         │
│  ├── UserRepository                                              │
│  └── TaskRepository                                              │
└──────────────────────────┬──────────────────────────────────────┘
                           │ SQL Queries
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                                │
│  MySQL Database                                                 │
│  ├── Users Table                                                 │
│  └── Tasks Table                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Layered Architecture

### 1. **Presentation Layer (Controllers)**

**Responsibility**: Handle HTTP requests and responses

**Components**:
- `AuthController` - Authentication endpoints
- `UserController` - User management endpoints
- `TaskController` - Task management endpoints
- `DashboardController` - Dashboard statistics

**Key Features**:
- RESTful API design
- Input validation with `@Valid` annotation
- HTTP status codes (200, 201, 400, 401, 404, 500)
- Request/response logging

```java
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @PostMapping
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(...) { }
}
```

---

### 2. **Security Layer**

**Responsibility**: Handle authentication and authorization

**Components**:
- `JwtUtil` - JWT token generation and validation
- `JwtAuthenticationFilter` - Filter for JWT validation
- `CustomUserDetailsService` - Load user details
- `SecurityConfiguration` - Spring Security config

**Flow**:
```
Request with JWT Token
    ↓
JwtAuthenticationFilter
    ↓
Extract & Validate Token
    ↓
Load UserDetails
    ↓
Set Authentication Context
    ↓
Allow/Deny Request
```

**JWT Structure**:
```
Header.Payload.Signature

Header: {
  "alg": "HS512",
  "typ": "JWT"
}

Payload: {
  "sub": "username",
  "iat": 1687501400,
  "exp": 1687587800
}
```

---

### 3. **Business Logic Layer (Services)**

**Responsibility**: Implement business rules and workflows

**Key Principles**:
- One service = one domain entity
- DTO in, DTO out
- No direct entity exposure
- Transaction management
- Exception handling

**Components**:

#### UserService
```java
- register()           // Create new user with validation
- getUserById()        // Retrieve user by ID
- getUserByUsername()  // Retrieve user by username
- updateUser()         // Update user profile
- isUsernameExists()   // Check username availability
- isEmailExists()      // Check email availability
```

#### AuthService
```java
- register()           // Call UserService to register
- login()              // Authenticate and generate JWT
- validateToken()      // Validate JWT token
- getUsernameFromToken() // Extract username from token
- generateToken()      // Generate new JWT token
```

#### TaskService
```java
- createTask()         // Create task with validation
- getTaskById()        // Get single task
- getAllTasksByUser()  // Get paginated tasks
- getTasksByStatus()   // Filter by status
- getTasksByPriority() // Filter by priority
- getTasksByCategory() // Filter by category
- searchTasks()        // Full-text search
- updateTask()         // Update task
- markTaskAsCompleted()// Mark as complete
- deleteTask()         // Delete task
- getOverduetasks()    // Get overdue tasks
- getDashboard()       // Get statistics with AI recommendations
- getTasksDueInRange() // Get tasks in date range
```

#### AIRecommendationEngine
```java
- getRecommendation() // Generate productivity recommendations
- suggestPriority()   // Suggest priority based on due date
- estimateDifficulty()// Estimate task difficulty
```

---

### 4. **Data Access Layer (Repositories)**

**Responsibility**: Interact with database

**Pattern**: Spring Data JPA Repository

#### UserRepository
```java
Optional<User> findByUsername(String username)
Optional<User> findByEmail(String email)
boolean existsByUsername(String username)
boolean existsByEmail(String email)
```

#### TaskRepository
```java
Page<Task> findByUserId(Long userId, Pageable pageable)
List<Task> findByUserIdAndStatus(Long userId, TaskStatus status)
Page<Task> findByUserIdAndPriority(Long userId, Priority priority, Pageable pageable)
Page<Task> searchTasks(Long userId, String keyword, Pageable pageable)
List<Task> findOverdueTasksByUserId(Long userId, TaskStatus status, LocalDateTime now)
List<Task> findTasksBetweenDates(Long userId, LocalDateTime startDate, LocalDateTime endDate)
```

---

### 5. **Entity Layer (Domain Models)**

**Responsibility**: Represent database entities

**Relationship**:
```
User (1) ─────── (Many) Tasks
  ├─ id
  ├─ username
  ├─ email
  ├─ password
  ├─ role
  └─ tasks

Task
  ├─ id
  ├─ title
  ├─ description
  ├─ priority
  ├─ status
  ├─ dueDate
  ├─ category
  ├─ user (FK)
  └─ timestamps
```

**Key Features**:
- Lombok annotations for boilerplate
- Hibernate annotations for ORM
- Audit timestamps (createdAt, updatedAt)
- Business logic methods (isOverdue(), isDueSoon(), etc.)

---

### 6. **DTO Layer (Data Transfer Objects)**

**Purpose**: 
- Decouple API contracts from entity structure
- Only expose necessary fields
- Provide request/response shapes
- Enable API versioning

**Types**:

**Request DTOs**:
- `RegisterRequest` - User registration
- `LoginRequest` - User login
- `CreateTaskRequest` - Create task
- `UpdateTaskRequest` - Update task

**Response DTOs**:
- `UserDTO` - User information
- `TaskDTO` - Task information
- `LoginResponse` - Login response with token
- `ApiResponse<T>` - Generic API response wrapper
- `DashboardDTO` - Dashboard statistics

```java
@Data
public class ApiResponse<T> {
    boolean success;
    String message;
    T data;
    LocalDateTime timestamp;
    Integer statusCode;
    String error;
}
```

---

### 7. **Exception Handling Layer**

**Approach**: Global exception handling with `@RestControllerAdvice`

**Exception Types**:

```
Exception
├── ResourceNotFoundException  (404)
│   └── Resource not found in database
│
├── BadRequestException        (400)
│   └── Invalid request data
│
├── AuthenticationException    (401)
│   └── Auth failed
│
├── MethodArgumentNotValidException (400)
│   └── Validation failed
│
└── Exception (catch-all)      (500)
    └── Unexpected server error
```

**Response Format**:
```json
{
  "success": false,
  "message": "User not found",
  "error": "RESOURCE_NOT_FOUND",
  "statusCode": 404,
  "timestamp": "2026-05-23T10:30:00"
}
```

---

### 8. **Configuration Layer**

**Components**:

#### SecurityConfiguration
```java
- securityFilterChain()    // Configure HTTP security
- passwordEncoder()        // Bean for BCrypt
- authenticationManager()  // Bean for auth manager
```

#### SwaggerConfiguration
```java
- customOpenAPI()          // Swagger/OpenAPI configuration
```

#### ApplicationConfiguration
```java
- Component scanning
- JPA repository enablement
- Transaction management
```

---

## Data Flow Examples

### Registration Flow
```
1. Client sends POST /auth/register with RegisterRequest
2. AuthController receives request
3. Validation happens via @Valid annotation
4. AuthService.register() is called
5. UserService.register() creates new User
6. UserRepository.save() persists to database
7. User is converted to UserDTO
8. ApiResponse<UserDTO> is returned with 201 status
```

### Login Flow
```
1. Client sends POST /auth/login with LoginRequest
2. AuthController receives request
3. AuthService.login() is called
4. UserRepository.findByUsername() retrieves user
5. Password is validated with PasswordEncoder
6. JwtUtil.generateToken() creates JWT
7. LoginResponse with token is returned
8. Client stores JWT token
```

### Task Creation Flow
```
1. Client sends POST /tasks with Authorization header
2. JwtAuthenticationFilter extracts & validates JWT
3. CustomUserDetailsService loads user details
4. SecurityContext is set with user info
5. TaskController.createTask() receives request
6. TaskService.createTask() validates input
7. AIRecommendationEngine suggests priority if needed
8. TaskRepository.save() persists task
9. Task is converted to TaskDTO
10. ApiResponse<TaskDTO> is returned with 201 status
```

### Task Search Flow
```
1. Client sends GET /tasks/search?keyword=...
2. Security & authentication checks
3. TaskController.searchTasks() receives keyword
4. TaskService.searchTasks() is called
5. TaskRepository.searchTasks() runs SQL query
6. Results are paginated
7. Tasks are converted to TaskDTOs
8. Page<TaskDTO> is returned
```

### Dashboard Flow
```
1. Client sends GET /dashboard/statistics
2. Security & authentication checks
3. DashboardController.getDashboard() receives request
4. TaskService.getDashboard() is called
5. TaskRepository queries are executed:
   - countByUserId()                    → total tasks
   - countByUserIdAndStatus(COMPLETED) → completed
   - countByUserIdAndStatus(PENDING)   → pending
   - countOverdueTasksByUserId()       → overdue
   - countByUserIdAndPriority()        → priority counts
6. AIRecommendationEngine.getRecommendation() generates advice
7. DashboardDTO is populated with statistics
8. ApiResponse<DashboardDTO> is returned
```

---

## Database Schema Relationships

```sql
-- Users Table (Master)
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN'),
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Tasks Table (Detail)
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description LONGTEXT,
    priority ENUM('LOW', 'MEDIUM', 'HIGH'),
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'),
    due_date DATETIME,
    category VARCHAR(100),
    completed_at DATETIME,
    user_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority)
);
```

---

## Request/Response Cycle

```
┌─────────────────────────────────────────────────────────┐
│ CLIENT REQUEST                                          │
│ POST /api/tasks                                         │
│ Authorization: Bearer jwt_token                         │
│ Content-Type: application/json                          │
│ { "title": "Task", "priority": "HIGH" }               │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 1. SERVLET FILTER                                       │
│ → JwtAuthenticationFilter processes request            │
│ → Extracts JWT token from header                       │
│ → Validates token with JwtUtil                         │
│ → Sets SecurityContext with user details               │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 2. DISPATCHER SERVLET                                   │
│ → Maps request to TaskController.createTask()          │
│ → Argument resolvers process @RequestBody              │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 3. VALIDATION                                           │
│ → @Valid triggers validation                           │
│ → @NotBlank, @Size validators execute                  │
│ → If invalid → 400 Bad Request response                │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 4. CONTROLLER                                           │
│ TaskController.createTask()                             │
│ → Extracts CreateTaskRequest DTO                       │
│ → Calls TaskService.createTask()                       │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 5. SERVICE LAYER                                        │
│ TaskService.createTask()                                │
│ → Validates business rules                             │
│ → Builds Task entity                                   │
│ → Calls TaskRepository.save()                          │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 6. REPOSITORY LAYER                                     │
│ TaskRepository.save()                                   │
│ → Converts entity to SQL INSERT                        │
│ → Executes query                                       │
│ → Returns persisted entity with ID                     │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 7. DATABASE                                             │
│ MySQL executes INSERT statement                        │
│ → Generates ID (auto-increment)                        │
│ → Stores data                                          │
│ → Returns result                                       │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 8. DTO CONVERSION                                       │
│ → Task entity converted to TaskDTO                     │
│ → Only necessary fields included                       │
│ → AI recommendations can be added                      │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 9. RESPONSE WRAPPING                                    │
│ → ApiResponse<TaskDTO> created                         │
│ → success = true                                       │
│ → message = "Task created successfully"                │
│ → statusCode = 201                                     │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 10. SERIALIZATION & RESPONSE                            │
│ → ApiResponse converted to JSON                        │
│ → HTTP 201 Created returned                            │
│ → Response sent to client                              │
└─────────────────────────────────────────────────────────┘
```

---

## Security Architecture

```
┌─────────────────────────────────────────────────────┐
│ AUTHENTICATION FLOW                                 │
└─────────────────────────────────────────────────────┘

1. CLIENT INITIATES LOGIN
   POST /auth/login
   { "username": "john", "password": "pass123" }
          │
          ▼
2. CONTROLLER RECEIVES REQUEST
   AuthController.login()
          │
          ▼
3. PASSWORD VERIFICATION
   PasswordEncoder.matches(inputPassword, storedHash)
          │
          ▼
4. JWT GENERATION
   JwtUtil.generateToken("john")
   - Creates header with algorithm
   - Creates payload with username & expiration
   - Signs with secret key
   - Returns: eyJhbGciOiJIUzUxMiJ9...
          │
          ▼
5. RESPONSE WITH TOKEN
   { "token": "eyJhbGciOiJIUzUxMiJ9...", "expiresIn": 86400000 }
          │
          ▼
6. CLIENT STORES TOKEN
   localStorage or session storage
```

```
┌─────────────────────────────────────────────────────┐
│ AUTHORIZATION FLOW (SUBSEQUENT REQUESTS)            │
└─────────────────────────────────────────────────────┘

1. CLIENT INCLUDES JWT IN HEADER
   Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
          │
          ▼
2. FILTER INTERCEPTS REQUEST
   JwtAuthenticationFilter.doFilterInternal()
          │
          ▼
3. EXTRACT TOKEN FROM HEADER
   Removes "Bearer " prefix
   Gets raw JWT string
          │
          ▼
4. VALIDATE SIGNATURE
   JwtUtil.validateToken()
   - Parses JWT
   - Verifies signature with secret key
   - Checks expiration
          │
          ▼
5. EXTRACT USERNAME FROM PAYLOAD
   JwtUtil.getUsernameFromToken()
   - Gets "sub" field from payload
   - Returns username
          │
          ▼
6. LOAD USER DETAILS
   CustomUserDetailsService.loadUserByUsername()
   - Queries database
   - Returns UserDetails with authorities
          │
          ▼
7. SET SECURITY CONTEXT
   SecurityContextHolder.getContext().setAuthentication()
   - Creates authentication token
   - Sets with UserDetails
   - Makes available to request
          │
          ▼
8. CONTINUE REQUEST PROCESSING
   Request proceeds to controller
   with user context available
          │
          ▼
9. AUTHORIZATION CHECK
   @PreAuthorize or method-level security
   - Checks user roles
   - Validates permissions
          │
          ▼
10. EXECUTE ENDPOINT
    If authorized → controller method executes
    If not → 403 Forbidden response
```

---

## Performance Considerations

### Database Optimization
1. **Indexes**: Created on frequently queried columns
   - `idx_user_id` - Fast lookup by user
   - `idx_status` - Filter by status
   - `idx_priority` - Filter by priority

2. **Pagination**: All list endpoints support pagination
   - Reduces memory usage
   - Improves response time

3. **Query Optimization**: Custom JPQL queries where needed
   - Avoids N+1 problems
   - Uses JOIN FETCH for relationships

### Caching Opportunities
- JWT token validation results
- User details during request
- Dashboard statistics (periodic refresh)

### Connection Pooling
- HikariCP with configurable pool size
- Connection timeout management
- Idle connection handling

---

## Extensibility Points

### Adding New Features
1. **New Entity**: Create entity → repository → service → controller
2. **New API**: Add controller endpoint → service method → repository query
3. **New Validation**: Add annotation or validator class
4. **New Security**: Extend SecurityConfiguration

### Scaling Considerations
1. Implement caching layer (Redis)
2. Add message queue (RabbitMQ, Kafka)
3. Implement API rate limiting
4. Add pagination for all list endpoints ✓ (already done)
5. Database replication & sharding
6. Microservices architecture (future)

---

## Testing Strategy

### Unit Tests
- Service layer tests with Mockito
- Test business logic in isolation
- Mock repositories and external dependencies

### Integration Tests
- Controller tests with MockMvc
- Test full request/response cycle
- Use in-memory database (H2)

### Test Pyramid
```
        ▲
       /|\  E2E Tests (Few)
      / | \
     /  |  \
    /───┼───\
   /    |    \  Integration Tests (Some)
  /     |     \
 /───────┼─────\ 
/        |      \ Unit Tests (Many)
```

---

## Deployment Architecture

```
┌──────────────────────────────────────────┐
│ Spring Boot Application (WAR/JAR)        │
│ - Embedded Tomcat                        │
│ - All dependencies included              │
└──────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│ Runtime Environment                      │
│ - Java 17 JVM                            │
│ - System libraries                       │
└──────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│ Network Layer                            │
│ - Reverse proxy (Nginx)                  │
│ - Load balancer                          │
│ - SSL/TLS termination                    │
└──────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│ Infrastructure                           │
│ - Container (Docker)                     │
│ - Orchestration (Kubernetes)             │
│ - Cloud provider (AWS, GCP, Azure)       │
└──────────────────────────────────────────┘
```

---

## Conclusion

This architecture provides:
- ✅ Clean separation of concerns
- ✅ Scalability through layering
- ✅ Security through JWT & Spring Security
- ✅ Maintainability through DTOs & services
- ✅ Extensibility through configuration
- ✅ Performance through indexing & pagination
- ✅ Reliability through exception handling

All requirements have been met with production-quality code following SOLID principles and Spring Boot best practices.
