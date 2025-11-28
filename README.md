# Review Microservice

A RESTful microservice built with Spring Boot for managing product reviews in the **HouseOfChaos** e-commerce application. This service provides a complete CRUD API for review management and is designed to be consumed by the main application via a Feign client.

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)

## 🎯 Overview

This microservice handles all review-related operations for products in the HouseOfChaos e-commerce platform. It provides endpoints for creating, retrieving, and deleting reviews, with support for querying reviews by product (subject) ID.

**Key Features:**
- RESTful API with proper HTTP status codes
- Input validation using Jakarta Bean Validation
- Global exception handling with Problem Details (RFC 7807)
- Comprehensive test coverage (Unit, Integration, and API tests)
- Separate database instance for isolation

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.4.0**
- **Spring Web** - REST API
- **Spring Data JPA** - Database persistence
- **Spring Validation** - Input validation
- **MySQL** - Production database
- **H2 Database** - In-memory database for testing
- **Lombok** - Boilerplate code reduction
- **Gradle** - Build tool
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher
- **Gradle** (or use the included Gradle Wrapper)
- **MySQL 8.0+** (for production/development)
- **Git** (for cloning the repository)

## 🚀 Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd review-microservice
```

### 2. Database Setup

Create a MySQL database for the review service:

```sql
CREATE DATABASE review_service;
```

Or the application will create it automatically if the user has proper permissions (see configuration below).

### 3. Environment Variables

The application requires the following environment variables for database connection:

**Windows (PowerShell):**
```powershell
$env:DB_USERNAME="your_mysql_username"
$env:DB_PASSWORD="your_mysql_password"
```

**Windows (Command Prompt):**
```cmd
set DB_USERNAME=your_mysql_username
set DB_PASSWORD=your_mysql_password
```

**Linux/macOS:**
```bash
export DB_USERNAME=your_mysql_username
export DB_PASSWORD=your_mysql_password
```

**Default Values:**
If environment variables are not set, the application will use:
- `DB_USERNAME`: `username`
- `DB_PASSWORD`: `password`

**Note:** Make sure your MySQL user has permissions to create databases if you want the application to create the database automatically.

### 4. Database Configuration

The application is configured to:
- Connect to MySQL at `localhost:3306`
- Use database name: `review_service`
- Automatically create the database if it doesn't exist
- Use Hibernate's `update` strategy for schema management

Configuration file: `src/main/resources/application-dev.properties`

## ▶️ Running the Application

### Using Gradle Wrapper

**Windows:**
```bash
.\gradlew bootRun
```

**Linux/macOS:**
```bash
./gradlew bootRun
```

### Using IDE

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Ensure Java 17 is configured
3. Run the `ReviewMicroserviceApplication` class

### Application Properties

The application runs on port **8081** by default.

Once started, the service will be available at:
```
http://localhost:8081
```

## 📡 API Documentation

### Base URL
```
http://localhost:8081/api/v1/reviews
```

### Endpoints

#### 1. Get Review by ID
```http
GET /api/v1/reviews/{id}
```

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "authorId": "550e8400-e29b-41d4-a716-446655440001",
  "subjectId": "550e8400-e29b-41d4-a716-446655440002",
  "body": "Great product, highly recommend!"
}
```

**Error:** `404 Not Found` - Review not found

---

#### 2. Get All Reviews by Subject ID
```http
GET /api/v1/reviews/subject/{id}
```

**Response:** `200 OK`
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "authorId": "550e8400-e29b-41d4-a716-446655440001",
    "subjectId": "550e8400-e29b-41d4-a716-446655440002",
    "body": "First review"
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440003",
    "authorId": "550e8400-e29b-41d4-a716-446655440004",
    "subjectId": "550e8400-e29b-41d4-a716-446655440002",
    "body": "Second review"
  }
]
```

**Note:** Returns empty array `[]` if no reviews exist for the subject.

---

#### 3. Create Review
```http
POST /api/v1/reviews
Content-Type: application/json
```

**Request Body:**
```json
{
  "authorId": "550e8400-e29b-41d4-a716-446655440001",
  "subjectId": "550e8400-e29b-41d4-a716-446655440002",
  "body": "Excellent quality product!"
}
```

**Validation Rules:**
- `authorId`: Required, must be a valid UUID
- `subjectId`: Required, must be a valid UUID
- `body`: Required, must not be blank

**Response:** `201 Created`
- Location header: `/api/v1/reviews/{generated-id}`
- Response body: Created review object

**Error:** `400 Bad Request` - Validation errors

---

#### 4. Delete Review
```http
DELETE /api/v1/reviews/{id}
```

**Response:** `204 No Content`

**Error:** `404 Not Found` - Review not found

### Error Responses

All error responses follow the Problem Details format (RFC 7807):

**404 Not Found:**
```json
{
  "type": "about:blank",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Review with ID: {id} not found",
  "timestamp": "2024-01-01T12:00:00Z"
}
```

**400 Bad Request (Validation Error):**
```json
{
  "type": "about:blank",
  "title": "Validation Error",
  "status": 400,
  "detail": "One or more fields have invalid values",
  "timestamp": "2024-01-01T12:00:00Z",
  "errors": {
    "authorId": "Author ID is required",
    "body": "Review body is required"
  }
}
```

## 🧪 Testing

The project includes comprehensive test coverage with three types of tests:

### Test Types

1. **Unit Tests** (`*UTest.java`)
   - Test service layer with mocked dependencies
   - Located in: `src/test/java/com/antdevrealm/reviewmicroservice/service/ReviewServiceUTest.java`

2. **Integration Tests** (`*ITest.java`)
   - Test service layer with real H2 in-memory database
   - Located in: `src/test/java/com/antdevrealm/reviewmicroservice/service/ReviewServiceITest.java`

3. **API Tests** (`*ATest.java`)
   - Test REST endpoints using MockMvc
   - Located in: `src/test/java/com/antdevrealm/reviewmicroservice/web/ControllerATest.java`

### Running Tests

**Run all tests:**
```bash
./gradlew test
```

**Run specific test class:**
```bash
./gradlew test --tests ReviewServiceUTest
```

**View test reports:**
After running tests, reports are available at:
```
build/reports/tests/test/index.html
```

### Test Coverage

The project maintains **88% line coverage** with a balanced distribution across:
- Unit tests
- Integration tests
- API tests

### Test Database

Tests use **H2 in-memory database** configured in `src/test/resources/application.properties`. No external database setup is required for running tests.

## 🔧 Configuration

### Application Properties

**Development Configuration** (`src/main/resources/application-dev.properties`):
- Server port: `8081`
- Database: MySQL (`review_service`)
- JPA: Hibernate with `update` strategy
- Database auto-creation: Enabled

**Test Configuration** (`src/test/resources/application.properties`):
- Database: H2 in-memory
- JPA: Hibernate with `create-drop` strategy

## 📝 Notes for Examiners

- All tests are passing and maintain 88% code coverage
- The application uses environment variables for database credentials (see [Setup Instructions](#setup-instructions))
- Tests are isolated and use H2 in-memory database - no external setup required
- The service follows RESTful principles with proper HTTP status codes
- Exception handling uses RFC 7807 Problem Details format
- Input validation is implemented using Jakarta Bean Validation

## 🤝 Integration

This microservice is designed to be consumed by the main HouseOfChaos application via:
- **Feign Client** (HTTP client)
- **REST API** calls

The service maintains its own database instance for complete isolation and scalability.

---

**Built with Spring Boot 3.4.0 | Java 17 | Gradle**
