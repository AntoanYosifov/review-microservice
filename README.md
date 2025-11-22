# Review Microservice

Spring Boot microservice for managing product reviews for the **HouseOfChaos** e-commerce application.

This service exposes a REST API and will be consumed by the main application via a Feign client.

## Tech Stack

- Java 17
- Spring Boot 3.4.0
- Spring Web
- Spring Data JPA
- Spring Validation
- MySQL
- Lombok
- Gradle

## Database

The service uses its **own database**, separate from the main application:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/review_service?createDatabaseIfNotExist=true
spring.datasource.username=${DB_USERNAME:username}
spring.datasource.password=${DB_PASSWORD:password}
spring.jpa.hibernate.ddl-auto=update