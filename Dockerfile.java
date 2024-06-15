# Dockerfile for Java application
FROM openjdk:11-jdk-slim

WORKDIR /app

# Copy Gradle wrapper and project files
COPY gradle /app/gradle
COPY gradlew /app/gradlew
COPY build.gradle /app/
COPY settings.gradle /app/
COPY src /app/src

# Build the application
RUN ./gradlew build

# Run the application
CMD ["java", "-cp", "build/libs/*", "com.example.MockLogGenerator"]

