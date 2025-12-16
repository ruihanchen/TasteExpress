# Tech Stack

The system is built with a modern Java backend stack focused on clarity, maintainability, and production-aligned practices.

---

## 1. Backend

**Language**  
- Java 21 (LTS)

**Framework**  
- Spring Boot 3.x  
  Primary application framework with auto-configuration and production-ready conventions.

**HTTP Layer**  
- Spring Web (Spring MVC)  
  RESTful, synchronous HTTP handling.

**Data Access**  
- Spring Data JPA + Hibernate  
  ORM-based persistence with clean domain modeling and reduced boilerplate.

---

## 2. Database

**Relational Database**  
- MySQL 8.x (InnoDB)  
  Optimized for transactional workloads.

**Schema Migrations**  
- Flyway  
  Version-controlled schema evolution with repeatable and consistent migrations.

---

## 3. Build, Testing, and Tooling

**Build Tool**  
- Maven

**Testing**  
- JUnit 5 — unit tests for domain and service logic  
- Spring Boot Test — integration tests for API and persistence flows

**Static Analysis (Optional)**  
- Checkstyle / SpotBugs / SonarLint

---

## 4. Runtime & Infrastructure

**Containerization**  
- Docker  
  Used for running MySQL locally and packaging the application.

**Local Environment**  
- docker-compose (optional)  
  For orchestrating MySQL and optional Redis/message broker containers.

---

## 5. API Documentation

- Springdoc OpenAPI  
  Automatically generated OpenAPI/Swagger documentation for all REST endpoints.

---

## 6. Optional Extensions (Phase 2+)

Optional components used to demonstrate engineering depth and extensibility.

### Caching  
- Redis  
  For read-heavy endpoints such as merchant listing and menus.

### Messaging / Async Processing  
- Kafka or RabbitMQ  
  For domain events and asynchronous workflows.

### Observability  
- Micrometer + Prometheus (metrics)  
- Structured logging for lifecycle events and error analysis

---

## 7. Deliberate Exclusions

The system intentionally avoids unnecessary complexity, including:

- Reactive frameworks (WebFlux)  
- Multiple database technologies  
- Heavy distributed infrastructure (Kubernetes, service mesh) in early phases

---

## Summary

The selected technologies provide a clean, maintainable, and production-aligned foundation suitable for building a backend service with clear domain logic and extensible architecture.
