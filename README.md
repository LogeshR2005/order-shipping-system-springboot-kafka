# 🚚 Order & Shipping Event-Driven Microservices

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-3.6.0-black.svg)](https://kafka.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)

## 📋 Overview

This project implements an **event-driven microservices architecture** using **Apache Kafka** as the message broker. It demonstrates asynchronous communication between two independent Spring Boot applications:

- **Order Service** - Manages customer orders and publishes order events
- **Shipping Service** - Handles shipping logistics and listens for new orders

The services communicate through Kafka topics, enabling loose coupling, scalability, and fault tolerance.

## 🏗️ Architecture

sequenceDiagram
    participant Client
    participant OrderService
    participant OrderDB
    participant Kafka
    participant ShippingService
    participant ShippingDB

    Client->>OrderService: POST /api/orders/produce
    OrderService->>OrderDB: save order
    OrderService->>Kafka: send to order_topic
    Kafka-->>ShippingService: consume order
    ShippingService->>ShippingDB: save shipping record
    Client->>ShippingService: DELETE /api/shipping/shipping/{orderId}
    ShippingService->>Kafka: send orderId to shipped_order_topic
    ShippingService->>ShippingDB: delete shipping record



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






