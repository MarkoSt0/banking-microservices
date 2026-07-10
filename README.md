# banking-microservices
Banking Microservices Platform is a modern, event-driven banking platform designed around a decoupled microservices architecture. The system simulates real-time ledger management, fund transfers, and asynchronous notifications, demonstrating high availability, fault tolerance, and clean separation of concerns.

The platform consists of three core microservices:
1. Account Service: Manages user accounts, currencies, and balances, exposing synchronous endpoints for balance updates while ensuring data integrity through a soft-delete mechanism.

2. Transaction Service: The heart of the platform. It handles fund transfer requests, orchestrates synchronous balance validations via REST, manages transaction states, and publishes financial events.

3. Notification Service: A completely decoupled consumer service that processes asynchronous event streams to log and simulate user notifications without impacting core transaction latency.

Tech Stack
- Backend: Java 21, Spring Boot 4, Spring Cloud OpenFeign

- Database & Migration: PostgreSQL, Flyway

- Messaging: Apache Kafka

- Resilience: Resilience4j (Circuit Breaker)

- Testing: Testcontainers, JUnit 5
