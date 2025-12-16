# Core User Flows & Order Lifecycle

This document describes the primary user flows for customers, merchants, and couriers, along with the corresponding order states used throughout the system.

---

## 1. Actors

- **Customer** – Places orders and tracks delivery status.
- **Merchant** – Manages menus and accepts or rejects incoming orders.
- **Courier** – Handles pickup and delivery tasks.
- **System Components** – Handle background processing such as dispatch logic.

---

## 2. Flow: Customer Places an Order

1. Customer selects a delivery address.
2. Customer browses available merchants.
3. Customer views a merchant’s menu.
4. Customer adds items to a cart and submits an order request.
5. Backend validates items and prices and creates an order in `CREATED`.
6. Payment is simulated, and the order moves to `PAID`.
7. Merchant is notified of the new order.
8. Customer can query the order to see its real-time status.

---

## 3. Flow: Merchant Accepts or Rejects an Order

1. Merchant views orders in `PAID`.
2. Merchant reviews details (items, pricing, delivery area).
3. Merchant chooses:
   - **Accept** → Order transitions to `ACCEPTED_BY_MERCHANT`
   - **Reject** → Order transitions to `REJECTED_BY_MERCHANT`
4. Accepted orders are made available for courier assignment.

---

## 4. Flow: Courier Accepts and Delivers an Order

1. Courier views available tasks.
2. Courier accepts a task → Order transitions to `ASSIGNED_TO_COURIER`.
3. Courier picks up the order from the merchant → `PICKED_UP`.
4. Courier delivers the order to the customer → `DELIVERED`.
5. All state changes are timestamped.

---

## 5. Flow: Customer Views Order History

1. Customer retrieves a list of:
   - Active orders  
   - Completed or cancelled historical orders
2. For each order, customer can view:
   - Items
   - Merchant information
   - Status and timestamps

---

## 6. Order State Machine (Initial Version)

The order lifecycle is modeled as a finite-state machine with valid transitions enforced by the backend.

**Primary states:**

- `CREATED`
- `PAID`
- `REJECTED_BY_MERCHANT`
- `ACCEPTED_BY_MERCHANT`
- `ASSIGNED_TO_COURIER`
- `PICKED_UP`
- `DELIVERED`
- `CANCELLED` (terminal state)

**Examples of valid transitions:**

- `CREATED → PAID`
- `PAID → ACCEPTED_BY_MERCHANT`  
- `PAID → REJECTED_BY_MERCHANT`
- `ACCEPTED_BY_MERCHANT → ASSIGNED_TO_COURIER`
- `ASSIGNED_TO_COURIER → PICKED_UP`
- `PICKED_UP → DELIVERED`

Invalid transitions (e.g., `DELIVERED → PICKED_UP`) are rejected by the service layer.

Each transition records:
- Actor responsible (customer, merchant, courier, system)
- Timestamp
