# Official Maven + Java 21 image
FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

# Copy files
COPY pom.xml .
COPY src ./src

# Build
RUN mvn clean compile

# Expose port
EXPOSE 8080

# Run your app
CMD ["mvn", "exec:java", "-Dexec.mainClass=QuoteApp"]
