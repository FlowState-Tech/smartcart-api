# Stage 1: Build
FROM eclipse-temurin:25-jdk AS builder

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


# Stage 2: Run
FROM eclipse-temurin:25-jdk

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]