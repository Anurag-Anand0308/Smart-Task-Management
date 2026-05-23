# Smart Task Management System - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+

---

## Step 1: Configure Database (1 minute)

### Option A: Using provided schema
1. Open MySQL Workbench or command line
2. Create database:
```sql
CREATE DATABASE smart_task_db;
```

### Option B: Using application startup
- The application will auto-create schema if `spring.jpa.hibernate.ddl-auto=update`

---

## Step 2: Update Configuration (1 minute)

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_task_db
spring.datasource.username=root
spring.datasource.password=root@123
jwt.secret=your_secure_jwt_key_change_in_production
```

---

## Step 3: Build Project (2 minutes)

```bash
cd d:\project\java project\smart-task-management
mvn clean build -DskipTests
```

---

## Step 4: Run Application (1 minute)

```bash
mvn spring-boot:run
```

**Expected Output:**
```
Started SmartTaskManagementApplication in X.XXX seconds
```

---

## Step 5: Test APIs

### Option A: Using Swagger UI (Recommended)
```
http://localhost:8080/api/swagger-ui.html
```

### Option B: Using Postman
1. Import `Smart-Task-Management-API.postman_collection.json`
2. Set `base_url = http://localhost:8080/api`
3. Execute requests

### Option C: Using cURL

#### 1. Login (get JWT token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"user@123"}'
```

#### 2. Create Task (replace with your token)
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title":"My First Task",
    "description":"This is a test task",
    "priority":"HIGH",
    "category":"Work"
  }'
```

#### 3. Get All Tasks
```bash
curl -X GET "http://localhost:8080/api/tasks?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 4. Get Dashboard
```bash
curl -X GET http://localhost:8080/api/dashboard/statistics \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📋 Sample Credentials

Login with pre-seeded users:

### Admin User
- **Username**: `admin`
- **Password**: `admin@123`
- **Email**: `admin@taskmanagement.com`

### Regular User
- **Username**: `user1`
- **Password**: `user@123`
- **Email**: `user1@taskmanagement.com`

---

## 🔑 API Endpoints Summary

### Authentication
```
POST   /auth/register      - Register new user
POST   /auth/login         - Login (returns JWT)
GET    /auth/health        - Health check
```

### Tasks
```
POST   /tasks              - Create task
GET    /tasks              - List tasks (paginated)
GET    /tasks/{id}         - Get task
PUT    /tasks/{id}         - Update task
DELETE /tasks/{id}         - Delete task
PUT    /tasks/{id}/complete - Mark completed
GET    /tasks/status/{status} - Filter by status
GET    /tasks/priority/{priority} - Filter by priority
GET    /tasks/search?keyword=... - Search tasks
GET    /tasks/overdue      - Get overdue tasks
```

### Dashboard
```
GET    /dashboard/statistics - Get statistics
```

### Users
```
GET    /users/profile      - Get current user
GET    /users/{id}         - Get user by ID
PUT    /users/{id}         - Update user
```

---

## 🧪 Quick Test Workflow

### 1. Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username":"testuser",
    "email":"test@example.com",
    "password":"Test@123",
    "confirmPassword":"Test@123"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Test@123"}'
```
**Save the returned `token`**

### 3. Create Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer [YOUR_TOKEN_HERE]" \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Learn Spring Boot",
    "description":"Master Spring Boot development",
    "priority":"HIGH",
    "dueDate":"2026-06-01T18:00:00",
    "category":"Learning"
  }'
```

### 4. Get Dashboard
```bash
curl -X GET http://localhost:8080/api/dashboard/statistics \
  -H "Authorization: Bearer [YOUR_TOKEN_HERE]"
```

---

## 📁 Project Structure at a Glance

```
smart-task-management/
├── src/main/java/com/taskmanagement/
│   ├── controller/          ← REST endpoints
│   ├── service/             ← Business logic
│   ├── repository/          ← Database queries
│   ├── entity/              ← Database models
│   ├── dto/                 ← Data transfer objects
│   ├── exception/           ← Error handling
│   ├── security/            ← JWT & auth
│   ├── config/              ← Configuration
│   ├── enums/               ← Enumerations
│   └── util/                ← Utilities
├── pom.xml                  ← Dependencies
└── src/main/resources/
    └── application.properties ← Configuration
```

---

## 🔧 Troubleshooting

### Port 8080 Already in Use
**Solution**: Change port in `application.properties`
```properties
server.port=8081
```

### Database Connection Error
**Solution**: Check MySQL is running and credentials are correct
```bash
mysql -u root -p
```

### Build Fails
**Solution**: Clear Maven cache
```bash
mvn clean install -U
```

### JWT Token Expired
**Solution**: Login again to get a new token
- Default expiration: 24 hours

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| README.md | Complete documentation |
| QUICK_START_GUIDE.md | This file - Quick setup |
| API_REQUESTS_RESPONSES.md | Detailed API examples |
| PROJECT_SUMMARY.md | Project overview |

---

## 🚦 Next Steps

1. ✅ Run the application
2. ✅ Test APIs using Swagger UI
3. ✅ Check out Postman collection
4. ✅ Review API examples
5. ✅ Explore source code
6. ✅ Read full README.md

---

## 💡 Tips

1. **Use Swagger UI** for interactive API testing
2. **Import Postman collection** for pre-built requests
3. **Check logs** in `logs/application.log` for debugging
4. **Enable DEBUG logging** in application.properties for more details

---

## 🆘 Need Help?

1. Check `README.md` for detailed documentation
2. Review `API_REQUESTS_RESPONSES.md` for examples
3. Check error responses in Swagger UI
4. Look at test files for usage examples

---

## 📞 Support

For issues or questions:
1. Check the documentation
2. Review error messages
3. Check application logs
4. Review test files for examples

---

**Happy Coding! 🎉**

For comprehensive documentation, see [README.md](README.md)
