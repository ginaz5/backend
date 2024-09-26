
# Book Rental Backend (Spring Boot + PostgreSQL)

## Introduction

This is the backend service for the Book Rental system built with **Spring Boot** and **Java 17**. It provides REST APIs for managing users, books, and rentals, and uses **PostgreSQL** for persistent storage.

---

## Prerequisites

Before starting the application, ensure the following tools are installed:

- **Java 17**: Install from [OpenJDK](https://openjdk.java.net/install/) or any preferred provider.
- **Maven**: Install Maven from [here](https://maven.apache.org/install.html) if not installed.
- **Docker**: Install Docker to run PostgreSQL in a container. [Get Docker](https://docs.docker.com/get-docker/).

---

## Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd backend
```

### 2. Start PostgreSQL with Docker

To run PostgreSQL using Docker, use the following command:

```bash
docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```

This will:
- Start a PostgreSQL container named `some-postgres`.
- Set the default password to `mysecretpassword`.

Check that the Docker container is running:

```bash
docker ps
```

### 3. Update Application Configuration

The application connects to PostgreSQL using the following configuration, which is located in `src/main/resources/application.properties`:

```properties
spring.application.name=backend

# Postgres database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=mysecretpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Ensure that this configuration matches your PostgreSQL setup. The database is assumed to be running on `localhost` with default credentials (`postgres` and `mysecretpassword`).

### 4. Build the Project

Install dependencies and build the project using Maven:

```bash
mvn clean install
```

### 5. Run the Application

You can start the Spring Boot application by running:

```bash
mvn spring-boot:run
```

Alternatively, package the project into a JAR and run it:

```bash
mvn package
java -jar target/book-rental-backend-0.0.1-SNAPSHOT.jar
```

---

## Access the API

Once the application is running, you can access the backend services at:

```
http://localhost:8080
```

You can use **Postman** or **cURL** to test the various API endpoints.

---

## Technologies Used

- **Java 17**: Backend built using Java 17 features.
- **Spring Boot**: Provides RESTful API services.
- **PostgreSQL**: Relational database used for data persistence.
- **Docker**: Used for containerizing PostgreSQL.
- **Maven**: Build and dependency management.

---