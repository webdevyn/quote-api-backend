FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean compile
EXPOSE 8080
CMD ["mvn", "exec:java", "-Dexec.mainClass=QuoteApp"]
