# ===== Build Stage =====
FROM eclipse-temurin:24-jdk AS build
WORKDIR /app

# Copy Gradle files first (for caching)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Pre-download dependencies
RUN ./gradlew dependencies --no-daemon || return 0

# Copy source code
COPY src ./src

# Build the jar
RUN ./gradlew bootJar --no-daemon --stacktrace --info

# ===== Runtime Stage =====
FROM eclipse-temurin:24-jre
WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose HTTP port (80 inside container)
EXPOSE 80

# Default environment variables (Spring Boot auto-detects these)
ENV SERVER_PORT=80
ENV THYMELEAF_CACHE=true
ENV SPRING_PROFILE=prod

# Launch the app
ENTRYPOINT ["java", "-jar", "app.jar"]
