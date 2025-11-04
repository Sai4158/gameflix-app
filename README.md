# GameFlix Backend - Dockerized (Spring Boot)
---

## 🚀 Quick Start

```bash
# 1) Build the Application JAR
./mvnw clean package -DskipTests        # macOS/Linux
.\\mvnw clean package -DskipTests        # Windows PowerShell

# 2) Build and Run Containers with Docker Compose
docker-compose up --build
```

> If Spring Security prints a **generated password** in the logs on first run, that’s expected for dev.  
> To stop a foreground container: **Ctrl + C** in that terminal.

---

## 🧰 Requirements

- **Docker** (Desktop or Engine)
- **Maven Wrapper** in your project (`mvnw`)
- Java 17 only needed locally if running the JAR **outside** Docker (Docker uses an OpenJDK base image).

---


## 🧪 Verify & Operate

You should see:

-   **Maven**: `BUILD SUCCESS`
-   **Docker Compose**: Both `gameflix-db` and `gameflix-app` containers are created and running. You'll see the Spring Boot banner and logs from the `app` service.

Useful Docker Compose commands:

```bash
# Follow the logs for a specific service (e.g., the app)
docker-compose logs -f app

# Stop the services without removing them
docker-compose stop

# Start the services again
docker-compose start

# Stop and remove all containers, networks, and volumes
docker-compose down

# To rebuild from scratch after code changes:
# (This is the same command as the Quick Start)
docker-compose up --build -d
```

---

## 🌐 Test the Container

Use **Postman** to test your API endpoints while the container is running.

**1) `POST /register`**
-   **URL**: `http://localhost:8080/register`
-   **Method**: `POST`
-   **Headers**: `Content-Type: application/json`
-   **Body** (raw JSON):
    ```json
    {
      "username": "Sai",
      "password": "Sai 1"
    }
    ```
-   **Expected**: `200 OK` with a JSON message (e.g., "Registered successfully" or your controller’s message).

**2) `POST /login`**
-   **URL**: `http://localhost:8080/login`
-   **Method**: `POST`
-   **Headers**: `Content-Type: application/json`
-   **Body** (raw JSON):
    ```json
    {
      "username": "Sai",
      "password": "Sai 1"
    }
    ```
-   **Expected**: `200 OK` with a JSON message (e.g., "Login successful"). If your app returns a token.


---

## 📦 Code Snippets 

### `Dockerfile` (Spring Boot)

```dockerfile
FROM eclipse-temurin:17-jre-jammy

RUN useradd -ms /bin/bash appuser
WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY --chown=appuser:appuser ${JAR_FILE} app.jar

USER appuser
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

```

### `.dockerignore`

```gitignore
target/*
!target/*.jar

.git
.gitignore
.idea
.vscode
*.iml
*.log
node_modules

```

### `docker-compose.yml`

```docker-compose.yml
services:
  db:
    image: mysql:8.0
    container_name: gameflix-db
    restart: unless-stopped
    environment:
      MYSQL_DATABASE: gameflix_db
      MYSQL_ROOT_PASSWORD: sqlroot4158
      MYSQL_USER: gameflix
      MYSQL_PASSWORD: gameflixpass
    command: >
      --default-authentication-plugin=caching_sha2_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_unicode_ci
    ports:
      - "3307:3306"
    volumes:
      - dbdata:/var/lib/mysql
    healthcheck:
      test: ["CMD-SHELL", "mysqladmin ping -uroot -psqlroot4158 -h localhost || exit 1"]
      interval: 5s
      timeout: 5s
      retries: 20

  app:
    build: .
    container_name: gameflix-app
    depends_on:
      db:
        condition: service_healthy
    environment:

      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/gameflix_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC

      SPRING_DATASOURCE_USERNAME: gameflix
      SPRING_DATASOURCE_PASSWORD: gameflixpass
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    ports:
      - "8080:8080"

volumes:
  dbdata:

```

---



