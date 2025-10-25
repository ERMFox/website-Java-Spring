# ===== Build Stage =====
FROM eclipse-temurin:24-jdk AS build
WORKDIR /app

# Copy Gradle wrapper and build scripts first (for caching)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Make gradlew executable (critical!)
RUN chmod +x gradlew

# Pre-download dependencies (ignore failure if no tests etc.)
RUN ./gradlew dependencies --no-daemon || true

# Copy the source code
COPY src ./src

# Build the jar (print full logs if it fails)
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
