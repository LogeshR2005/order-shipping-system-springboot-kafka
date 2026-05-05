
# 🚚 Order & Shipping Event-Driven Microservices

## 📋 Overview

This project implements an **event-driven microservices architecture** using **Apache Kafka** as the message broker. It demonstrates asynchronous communication between two independent Spring Boot applications:

- **Order Service** - Manages customer orders and publishes order events
- **Shipping Service** - Handles shipping logistics and listens for new orders

The services communicate through Kafka topics, enabling loose coupling, scalability, and fault tolerance.

## 🏗️ Architecture

<img width="1328" height="539" alt="Screenshot 2026-05-05 at 11 33 22 PM" src="https://github.com/user-attachments/assets/64d8f104-3c64-4fb6-8441-03691d9b329d" />

### Data Flow

1. **Order Creation**: Client → Order Service → PostgreSQL → Kafka (`order_topic`)
2. **Shipping Processing**: Kafka (`order_topic`) → Shipping Service → PostgreSQL
3. **Shipping Confirmation**: Client → Shipping Service → Kafka (`shipped_order_topic`)

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Core programming language |
| Spring Boot | 4.0.x | Application framework |
| Spring Kafka | 3.0.x | Kafka integration |
| Spring Data JPA | 3.0.x | Database operations |
| Apache Kafka | 3.6.0 | Message broker |
| PostgreSQL | 15 | Persistent storage |
| Lombok | 1.18.30 | Boilerplate reduction |
| Jackson | 2.15.x | JSON serialization |
| Maven | 3.9.x | Build automation |

## 📁 Project Structure

├── order-service
│   ├── AppConstants.java
│   ├── Order.java
│   ├── OrderApplication.java
│   ├── OrderController.java
│   ├── OrderProducer.java
│   └── OrderRepository.java
└── shipping-service
    ├── AppConstants.java
    ├── Shipping.java
    ├── ShippingApplication.java
    ├── ShippingConsumer.java
    ├── ShippingController.java
    ├── ShippingRepository.java
    └── ShippedOrderIdProducer.java





