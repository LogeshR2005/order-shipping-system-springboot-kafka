
# 📦 Order & Shipping Event-Driven Microservices

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
 
```
order-shipping-kafka/
│
├── order-service/
│   ├── AppConstants.java          # Kafka topic name constants
│   ├── Order.java                 # Order JPA entity
│   ├── OrderApplication.java      # Spring Boot entry point
│   ├── OrderController.java       # POST /api/orders/produce
│   ├── OrderProducer.java         # Publishes Order JSON to order_topic
│   └── OrderRepository.java       # JPA repository
│
├── shipping-service/
│   ├── AppConstants.java          # Kafka topic name constants
│   ├── Shipping.java              # Shipping JPA entity
│   ├── ShippingApplication.java   # Spring Boot entry point
│   ├── ShippingConsumer.java      # Consumes order_topic, saves Shipping record
│   ├── ShippingController.java    # GET /api/shipping, DELETE /api/shipping/shipping/{id}
│   ├── ShippingRepository.java    # JPA repository
│   └── ShippedOrderIdProducer.java # Publishes orderId (Long) to shipped_order_topic
│

└── README.md
```
 
---
 
## ⚙️ Configuration
 
### Order Service — `application.properties`
 
```properties
server.port=8081
spring.application.name=order-service
 
spring.datasource.url=jdbc:postgresql://localhost:5432/orderdb
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
 
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
```
 
### Shipping Service — `application.properties`
 
```properties
server.port=8082
spring.application.name=shipping-service
 
spring.datasource.url=jdbc:postgresql://localhost:5432/shippingdb
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
 
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=shipping-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.LongSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.LongSerializer
```
 
---

###  Setup
 
**Step 1: Start Kafka**
```bash
bin/kafka-server-start.sh config/server.properties
```
 
**Step 2: Create Topics**
```bash
bin/kafka-topics.sh --create --topic order_topic         --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
bin/kafka-topics.sh --create --topic shipped_order_topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```
 
**Step 3: Run Services**
```bash
# Terminal 1
cd order-service && mvn spring-boot:run
 
# Terminal 2
cd shipping-service && mvn spring-boot:run
```
 
---
 
## 📡 API Endpoints
 
### Order Service — `localhost:8081`
 
| Method | Endpoint              | Description                                      |
|--------|-----------------------|--------------------------------------------------|
| `POST` | `/api/orders/produce` | Saves order to DB and publishes to `order_topic` |
 
**Request Body:**
```json
{
  "customerName": "Jane Doe",
  "totalCost": 149.99,
  "address": "123 Main St, Chennai",
  "shipped": false
}
```
 
### Shipping Service — `localhost:8082`
 
| Method   | Endpoint                           | Description                                                       |
|----------|------------------------------------|-------------------------------------------------------------------|
| `GET`    | `/api/shipping`                    | Returns all pending shipping records                              |
| `DELETE` | `/api/shipping/shipping/{orderId}` | Publishes orderId to `shipped_order_topic` and deletes the record |
 
> The Shipping Service **automatically** creates a record when it consumes from `order_topic` — no explicit API call needed.
 
---
 
## 🧵 Kafka Topics
 
| Topic                 | Message Type        | Producer         | Consumer                                              |
|-----------------------|---------------------|------------------|-------------------------------------------------------|
| `order_topic`         | JSON String (Order) | Order Service    | Shipping Service (`ShippingConsumer`)                 |
| `shipped_order_topic` | Long (orderId)      | Shipping Service | *(extend with a Notification / Billing service)*      |
 
---
 
## 🧪 Quick Test
 
```bash
# 1. Create an order
curl -X POST http://localhost:8081/api/orders/produce \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Alice","totalCost":99.99,"address":"456 Elm St","shipped":false}'
 
# 2. Check shipping records
curl http://localhost:8082/api/shipping
 
# 3. Mark as shipped
curl -X DELETE http://localhost:8082/api/shipping/shipping/1
```
 
---
 
## ⚠️ Known Issues
 
| Issue | Fix |
|-------|-----|
| `tools.jackson.databind.ObjectMapper` import in consumer/producer | Replace with `com.fasterxml.jackson.databind.ObjectMapper` |
| `ShippingConsumer` directly injects `ShippingController` | Refactor to a `ShippingService` — controllers shouldn't act as the service layer |
| No consumer for `shipped_order_topic` | Implement a downstream service (e.g., notifications, billing) to consume it |
| No retry / dead-letter logic on consumers | Add `@RetryableTopic` or a DLT for fault tolerance in production |
 
---
 
## 📄 License
 
This project is for learning and demonstration purposes. Free to use and adapt.
 




