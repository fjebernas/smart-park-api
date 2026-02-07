# SmartPark API Project

SmartPark API is a Spring Boot application for managing parking lots, vehicles, and check-in/check-out operations.

---

## Requirements


* **Java 8** (JDK 8)
* **Maven 3.6+**

---

## Build Instructions

From project **root**:

```bash
mvn clean compile
```

---

##  Run Application

```bash
mvn spring-boot:run
```

or

```bash
mvn clean package
java -jar target/*.jar
```

Application will start on:

```
http://localhost:8080/api/smartpark/v1
```

---

## Run Tests

```bash
mvn clean test
```
---


## API Testing (Postman)

A ready-to-use **Postman collection** is included in the project:

See the **postman** folder at the **root** of the project.

---

**SmartPark API** by **fjcodes**
