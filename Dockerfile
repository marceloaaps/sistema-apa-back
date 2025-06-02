# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /app/target/back-0.0.1-SNAPSHOT.jar back.jar

ENTRYPOINT ["java", "-jar", "back.jar"]

