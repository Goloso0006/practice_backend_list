# To-Do List Backend

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://www.java.com/)
[![Spring Boot 4.0.5](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36.svg)](https://maven.apache.org/)
[![CI - To-Do List App](https://github.com/Goloso0006/practice_backend_list/actions/workflows/ci.yml/badge.svg)](https://github.com/Goloso0006/practice_backend_list/actions/workflows/ci.yml)

API REST desarrollada con Spring Boot para administrar una lista de tareas.
Permite crear, consultar, actualizar y eliminar tareas desde un cliente HTTP o desde un frontend que consuma la API.

## ¿Qué hace este proyecto?

Este backend expone endpoints REST para trabajar con la entidad `Task`.
Cada tarea almacena:

- `id`: identificador autogenerado
- `title`: título de la tarea
- `description`: descripción opcional o complementaria
- `completed`: estado de finalización

La aplicación sigue una arquitectura en capas para separar responsabilidades y utiliza Spring Data JPA para persistir la información en la base de datos.

## Características principales

- CRUD completo de tareas: crear, listar, actualizar y eliminar
- Estructura en capas: `Controller`, `Service`, `Repository` y `Model`
- Persistencia con JPA y ORM
- Soporte para base de datos H2 en desarrollo y MySQL como alternativa
- Uso de Maven Wrapper para ejecutar el proyecto sin instalar Maven globalmente
- Código preparado para pruebas unitarias e integración
- API sencilla de consumir desde Postman, frontend web o cualquier cliente HTTP

## Tecnologías usadas

- Java 17 (`java.version` definido en `pom.xml`)
- Spring Boot 4.0.5
- Spring Web MVC
- Spring Data JPA
- Lombok
- H2 Database (runtime)
- MySQL Connector/J (runtime)
- Maven y Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Estructura del proyecto

```text
backend/
├── src/main/java/com/todolist/backend/
│   ├── BackendApplication.java
│   ├── controller/
│   │   └── TaskController.java
│   ├── service/
│   │   └── TaskService.java
│   ├── repository/
│   │   └── TaskRepository.java
│   └── model/
│       └── Task.java
├── src/main/resources/
│   └── application.properties
├── src/test/java/com/todolist/backend/
│   ├── controller/
│   └── service/
├── pom.xml
└── mvnw / mvnw.cmd
```

## Cómo ejecutar el proyecto

### Requisitos previos

- Java 25 instalado
- Maven Wrapper disponible en el repositorio
- Base de datos configurada si vas a usar MySQL

### Ejecutar en modo desarrollo

En Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

La API quedará disponible en:

```text
http://localhost:8080/tasks
```

### Compilar y ejecutar el JAR

```powershell
.\mvnw.cmd clean package
java -jar .\target\backend-0.0.1-SNAPSHOT.jar
```

## Documentación relacionada

- `api.md`: documentación detallada de endpoints
- `architecture.md`: explicación de la arquitectura del backend
- `setup.md`: configuración del entorno y variables necesarias

## Autores

**Proyecto Backend - To-Do List**

- Marlon Alexander Imbajoa Canchala

## Licencia

Este proyecto está disponible bajo la licencia MIT.