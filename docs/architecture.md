# Architecture Overview

This document provides a formal overview of the system architecture for the Food Delivery Backend. The design follows established practices commonly used in large-scale Java backend services, emphasizing clarity, maintainability, and future extensibility.

---

## 1. Architectural Principles

The architecture is guided by the following principles:

- **Layered Structure:** Clear separation of concerns across API, Service, and Repository layers.
- **Domain-Centric Design:** Business logic is centralized in the service layer; domain entities model core concepts.
- **Stateless Application Layer:** All persistent state is externalized to MySQL.
- **Extensibility:** The system is designed to accommodate caching, asynchronous workflows, and multi-region considerations without significant restructuring.
- **Consistency and Traceability:** All order transitions follow a well-defined state machine with strict validation.

---

## 2. Layered Architecture

### 2.1 API Layer

The API layer exposes REST endpoints for customers, merchants, and couriers.

Responsibilities include:

- Handling HTTP requests and responses
- Input validation and authentication
- Role-based access checks
- Delegating business operations to the service layer

Controller groups:

- Authentication
- Customer order operations
- Merchant order operations
- Courier task operations
- Merchant and menu management

---

### 2.2 Service Layer

The service layer contains the core business logic and enforces domain rules.

Key responsibilities:

- Managing the order lifecycle and state transitions
- Validating business conditions beyond basic input checks
- Coordinating repository operations within transactional boundaries
- Encapsulating logic for merchant workflows and courier workflows
- Providing extension points for future asynchronous processing or event publishing

Representative services:

- `AuthService`
- `UserService`
- `MerchantService`
- `OrderService`
- `MerchantOrderService`
- `CourierService`
- `DispatchService`

All domain behavior is implemented here to ensure testability and maintainability.

---

### 2.3 Repository Layer

Repository interfaces abstract persistence using Spring Data JPA.

Responsibilities:

- CRUD operations on domain entities
- Querying for domain-specific data (e.g., orders for a merchant)
- Hiding JPA/Hibernate internals from the service layer

Repositories:

- `UserRepository`
- `MerchantRepository`
- `MenuItemRepository`
- `OrderRepository`
- `OrderItemRepository`
- `CourierRepository`

---

## 3. Domain Model Summary

### Core Entities

- **User** – Authentication credentials and role information  
- **Customer** – End user placing orders  
- **Merchant** – Store information and menu ownership  
- **Courier** – Delivery agent  
- **MenuItem** – Item offered by a merchant  
- **Order** – Customer order with lifecycle state  
- **OrderItem** – Individual line items within an order  

### Order Lifecycle (State Machine)

The lifecycle is modeled explicitly using a state machine with allowable transitions:

`CREATED → PAID → ACCEPTED_BY_MERCHANT → ASSIGNED_TO_COURIER → PICKED_UP → DELIVERED`

Terminal states:

- `REJECTED_BY_MERCHANT`
- `CANCELLED`

All transitions are validated and timestamped in the service layer.

---

## 4. External Components

### 4.1 Database (MySQL)

- Primary source of truth
- Stores users, merchants, menus, orders, and lifecycle events
- Managed with Flyway migrations for schema consistency

### 4.2 Caching (Optional: Redis)

Used for:

- Merchant listings
- Menu lookups

Integrated via encapsulated caching logic to avoid polluting domain code.

### 4.3 Messaging (Optional: Kafka/RabbitMQ)

Potential use cases:

- Emitting order events (e.g., accepted, delivered)
- Background processing
- Notifications or analytics

Messaging is optional but the architecture contains clear integration points.

---

## 5. Cross-Cutting Concerns

- **Authentication/Authorization:** Token-based access with role enforcement
- **Input Validation:** Request-level + business rules
- **Error Handling:** Centralized exception mapping to consistent responses
- **Logging:** Structured logs capturing key lifecycle transitions
- **Metrics (Optional):** Request counts, latency, error rates via Micrometer + Prometheus

---

## 6. Deployment Overview

Local development environment includes:

- Spring Boot application
- MySQL running in Docker
- Optional Redis and Kafka/RabbitMQ via Docker Compose

The application is container-ready and can be packaged into a Docker image.

---

## 7. Summary

This architecture reflects a typical backend service design used in production Java environments. It emphasizes clarity, testability, and extensibility while keeping the initial system implementation focused and manageable.
