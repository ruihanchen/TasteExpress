# Feature Scope

This document outlines the functional scope of the food delivery backend system, divided into MVP features and engineering-focused enhancements.

---

## 1. MVP Features

The MVP targets a single-city deployment and includes the essential functionality needed to support the full food delivery lifecycle.

### 1.1 Authentication & Roles

- User registration and login
- Role distinction: customer, merchant, courier
- Token-based authentication (JWT or equivalent)

### 1.2 Merchant & Menu Management

- Merchants manage their basic profile
- CRUD operations for menu items (name, description, price, availability)
- Customers can retrieve:
  - Active merchants
  - Menu items for a merchant

---

### 1.3 Order Creation & Payment Simulation

- Customers create orders from menu items
- System validates items and pricing
- Order created in `CREATED`
- Payment simulation triggers transition to `PAID`

---

### 1.4 Merchant Order Handling

- Merchants view newly paid orders
- Orders can be accepted or rejected
- Accepted → `ACCEPTED_BY_MERCHANT`  
- Rejected → `REJECTED_BY_MERCHANT`

---

### 1.5 Courier Assignment & Delivery Workflow

- Couriers view available tasks
- Accepting a task → `ASSIGNED_TO_COURIER`
- Pickup → `PICKED_UP`
- Delivery → `DELIVERED`
- All transitions validated and timestamped

---

### 1.6 Order Queries

- Customers can view active and historical orders
- Merchants can view current and past orders for their restaurant
- Couriers can view current tasks and completed deliveries

---

## 2. Engineering Depth Features (Phase 2+)

These features enhance maintainability, scalability, and production readiness.

### 2.1 Layered Architecture & Domain Modeling

- Dedicated API, Service, and Repository layers
- Explicit domain entities for users, merchants, couriers, orders, and order items
- Centralized validation and error handling

### 2.2 Database Migrations

- Schema versioning via Flyway
- Repeatable migrations for consistent environments

### 2.3 Logging & Observability

- Structured logging of major events
- Basic metrics (request counts, error rates, latency) using Micrometer + Prometheus (optional)

### 2.4 Caching (Optional)

- Redis for merchant list and menu caching
- Defined invalidation strategies

### 2.5 Messaging / Async Processing (Optional)

- Kafka or RabbitMQ for domain events (e.g., order accepted)
- Simple producer/consumer flow to demonstrate asynchronous design

### 2.6 Explicit Order State Machine

- Centralized state transition logic
- Validation of allowable transitions
- Comprehensive unit tests for state changes

---

## 3. Stretch Enhancements (Future Work)

- Multi-region or multi-city support
- Real payment integration (Stripe, etc.)
- Automatic courier dispatching with heuristics
- Enhanced access control and security models

---

## 4. Phase Summary

- **Phase 1**: Build the full MVP with clean architecture
- **Phase 2**: Add selected engineering depth features
- **Phase 3**: Optional extensions for scalability or system design demonstrations
