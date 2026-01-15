# Spring Boot Task Management API

A RESTful API for personal task management built with Spring Boot, featuring categories, priorities, and status tracking. This project demonstrates enterprise Spring Boot patterns including Spring Data JPA, Repository pattern, Service layer, and REST controllers.

## Features

- **Task Management**: Create, read, update, and delete tasks
- **Categories**: Organize tasks into categories
- **Priorities**: Assign priority levels (LOW, MEDIUM, HIGH, URGENT)
- **Status Tracking**: Track task status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
- **Due Dates**: Set and track task due dates
- **Overdue Detection**: Find tasks that are past their due date
- **Statistics**: Get task counts by status

## Technology Stack

- **Spring Boot 3.2.0**
- **Spring Data JPA** - Database abstraction layer
- **H2 Database** - In-memory database for development
- **PostgreSQL** - Production database support
- **Lombok** - Reduces boilerplate code
- **Maven** - Dependency management

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL (optional, for production)

## Getting Started

### Local Development (H2 Database)

1. Clone the repository:
```bash
git clone <repository-url>
cd spring-boot-task-api
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: (leave empty)

### Production (PostgreSQL)

1. Create a PostgreSQL database:
```sql
CREATE DATABASE taskdb;
```

2. Update `application.properties` or use profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

Or set environment variables:
```bash
export SPRING_PROFILES_ACTIVE=postgres
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/taskdb
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=yourpassword
```

## API Endpoints

### Health

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Get API health status and statistics |

### Categories

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Get all categories |
| GET | `/api/categories/{id}` | Get category by ID |
| POST | `/api/categories` | Create a new category |
| PUT | `/api/categories/{id}` | Update a category |
| DELETE | `/api/categories/{id}` | Delete a category |

### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks |
| GET | `/api/tasks/{id}` | Get task by ID |
| GET | `/api/tasks/status/{status}` | Get tasks by status |
| GET | `/api/tasks/priority/{priority}` | Get tasks by priority |
| GET | `/api/tasks/category/{categoryId}` | Get tasks by category |
| GET | `/api/tasks/overdue` | Get overdue tasks |
| GET | `/api/tasks/stats` | Get task statistics |
| POST | `/api/tasks` | Create a new task |
| PUT | `/api/tasks/{id}` | Update a task |
| PATCH | `/api/tasks/{id}/status` | Update task status |
| DELETE | `/api/tasks/{id}` | Delete a task |

## Example Requests

### Create a Category

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Work",
    "description": "Work-related tasks"
  }'
```

### Create a Task

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project documentation",
    "description": "Write comprehensive API documentation",
    "priority": "HIGH",
    "status": "PENDING",
    "category": {
      "id": 1
    },
    "dueDate": "2024-12-31T23:59:59"
  }'
```

### Get All Tasks

```bash
curl http://localhost:8080/api/tasks
```

### Update Task Status

```bash
curl -X PATCH http://localhost:8080/api/tasks/1/status \
  -H "Content-Type: application/json" \
  -d '"IN_PROGRESS"'
```

### Get Task Statistics

```bash
curl http://localhost:8080/api/tasks/stats
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/taskapi/
│   │   ├── TaskApiApplication.java      # Main application class
│   │   ├── controller/                  # REST controllers
│   │   │   ├── CategoryController.java
│   │   │   ├── TaskController.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── model/                       # JPA entities
│   │   │   ├── Category.java
│   │   │   └── Task.java
│   │   ├── repository/                  # Spring Data JPA repositories
│   │   │   ├── CategoryRepository.java
│   │   │   └── TaskRepository.java
│   │   └── service/                     # Business logic layer
│   │       ├── CategoryService.java
│   │       └── TaskService.java
│   └── resources/
│       ├── application.properties       # H2 configuration
│       └── application-postgres.properties  # PostgreSQL configuration
```

## Spring Boot Patterns Demonstrated

1. **Dependency Injection**: Using `@Autowired` and constructor injection
2. **Repository Pattern**: Spring Data JPA repositories
3. **Service Layer**: Business logic separation
4. **REST Controllers**: RESTful API design
5. **JPA Entities**: Database mapping with relationships
6. **Validation**: Bean validation with `@Valid`
7. **Exception Handling**: Global exception handler
8. **Transaction Management**: `@Transactional` annotations

## Database Schema

### Categories Table
- `id` (Primary Key)
- `name` (Unique, Not Null)
- `description`
- `created_at`
- `updated_at`

### Tasks Table
- `id` (Primary Key)
- `title` (Not Null)
- `description`
- `priority` (Enum: LOW, MEDIUM, HIGH, URGENT)
- `status` (Enum: PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
- `category_id` (Foreign Key to Categories)
- `due_date`
- `completed_at`
- `created_at`
- `updated_at`

## License

MIT License
