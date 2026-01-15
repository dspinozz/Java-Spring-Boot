# Quick Start Guide

## Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

## H2 Database Console

Access at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: (empty)

## Quick Test Commands

### 0. Health Check
```bash
curl http://localhost:8080/api/health
```

### 1. Create a Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Work", "description": "Work tasks"}'
```

### 2. Create a Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Study Spring Boot patterns",
    "priority": "HIGH",
    "status": "PENDING"
  }'
```

### 3. Get All Tasks
```bash
curl http://localhost:8080/api/tasks
```

### 4. Get Task Statistics
```bash
curl http://localhost:8080/api/tasks/stats
```

## H2 Database Console

Access at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: (empty)

## Key Spring Boot Patterns

This project demonstrates:
- ✅ Dependency Injection (`@Autowired`, constructor injection)
- ✅ Spring Data JPA (Repository pattern)
- ✅ Service Layer (Business logic separation)
- ✅ REST Controllers (RESTful API design)
- ✅ JPA Entities (Database mapping with relationships)
- ✅ Bean Validation (`@Valid`, `@NotBlank`, `@NotNull`)
- ✅ Global Exception Handling (`@RestControllerAdvice`)
- ✅ Transaction Management (`@Transactional`)
