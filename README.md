"# Spring-JWT-Auth-Service" 
# JWT + Role-Based Access Control (RBAC) Spring Boot Project

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)

---

## **Project Overview**
This Spring Boot application implements **JWT-based authentication** with **role-based access control (RBAC)**.  
Users can **signup, login**, and access endpoints based on their roles (`ROLE_USER`, `ROLE_ADMIN`).  
The project also includes **request validation**, **exception handling**, **logging**, and **transactional database operations** for robust APIs.

---

## **Key Features**
- **User Authentication & Authorization:** Signup and login with secure password encryption; JWT tokens issued with embedded roles.
- **Role-Based Endpoint Security:** Protected REST APIs using Spring Security; endpoints accessible only to authorized roles.
- **Database Integration:** Users and roles persisted in PostgreSQL using Spring Data JPA; default admin created at startup.
- **Transactional Operations:** Critical database methods marked with `@Transactional` for atomic operations.
- **Validations:** Request payloads validated using `@Valid` and annotations like `@NotBlank` and `@Size`.
- **Exception Handling:** Custom exceptions with global handler (`@ControllerAdvice`) returning meaningful HTTP status codes.
- **Logging:** SLF4J logging implemented for signup/login attempts, JWT issuance, and error tracking.

---

## **Technologies Used**
- **Language & Framework:** Java 17, Spring Boot 3.x, Spring Security, Spring Data JPA
- **Database:** PostgreSQL
- **Security:** JWT for authentication, role-based access control for authorisation
- **Logging & Validation:** SLF4J, Jakarta Bean Validation

---

## **Setup Instructions**
1. Clone the repository:
```bash
git clone <repo-url>
```

2.Configure PostgreSQL in application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb

spring.datasource.username=yourusername

spring.datasource.password=yourpassword


3.Build and run the project:

mvn clean install:

mvn spring-boot:run

## Default admin user is created at startup:

username: admin

password: admin123

role: ROLE_ADMIN

## **API Endpoints**

POST	 /api/auth/signup	 Register a new user	Public

POST	/api/auth/login	 Login and get JWT token	Public

GET	/api/admin/**	Admin-only endpoints	ROLE_ADMIN

Include JWT token in Authorization header for protected endpoints:


Authorization: Bearer <jwt-token>

## **Usage**
Signup and login to obtain JWT token.

Access protected endpoints using the token.

Admin endpoints restricted to users with ROLE_ADMIN.



