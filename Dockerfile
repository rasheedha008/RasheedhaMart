FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY schema.sql .
COPY seed.sql .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/rasheedha-mart-1.0.0.jar ./rasheedha-mart.jar
EXPOSE 8080
ENTRYPOINT ["java", "-cp", "rasheedha-mart.jar", "com.rasheedha.rasheedhamart.server.MainServer"]
