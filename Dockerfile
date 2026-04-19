# Etapa 1: compila el proyecto y genera el JAR dentro de Docker.
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml
RUN chmod +x mvnw

COPY src src
RUN ./mvnw clean package -DskipTests

# Etapa 2: imagen final ligera para ejecutar la app.
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

COPY --from=builder /app/target/*.jar app_list.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_list.jar"]