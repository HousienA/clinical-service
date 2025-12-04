# Multi-stage build for Spring Boot
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar
# Environment variables (can be overridden at runtime)
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8082
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
