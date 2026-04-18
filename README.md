# To-Do List Backend (Spring Boot)

API REST para gestionar tareas (crear, listar, actualizar y eliminar) de una lista To-Do.

## Descripcion general

Este proyecto expone endpoints HTTP para administrar tareas con la entidad `Task`.
Cada tarea tiene:

- `id` (Long, autogenerado)
- `title` (String)
- `description` (String)
- `completed` (Boolean)

La API sigue una arquitectura en capas (`Controller -> Service -> Repository -> DB`) y usa Spring Data JPA para persistencia.

## Tecnologias usadas

- Java (configurado en `pom.xml` con `java.version=25`)
- Spring Boot 4.0.5
- Spring Web MVC
- Spring Data JPA
- Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`)
- H2 (runtime)
- MySQL Connector/J (runtime)
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Como ejecutar el proyecto

1. Abre terminal en la raiz del proyecto.
2. Compila y ejecuta la aplicacion:

```powershell
.\mvnw.cmd spring-boot:run
```

3. La API queda disponible (por defecto) en:

`http://localhost:8080/tasks`

### Alternativa: empaquetar y ejecutar JAR

```powershell
.\mvnw.cmd clean package
java -jar .\target\backend-0.0.1-SNAPSHOT.jar
```

## Estructura basica del proyecto

```text
src/main/java/com/todolist/backend/
|- BackendApplication.java
|- controller/
|  `- TaskController.java
|- service/
|  `- TaskService.java
|- repository/
|  `- TaskRepository.java
`- model/
   `- Task.java

src/main/resources/
`- application.properties
```

## Ejemplo de uso general

### Crear una tarea

```http
POST /tasks
Content-Type: application/json

{
  "title": "Estudiar Spring",
  "description": "Revisar controller y service",
  "completed": false
}
```

### Obtener tareas

```http
GET /tasks
```

Respuesta esperada:

```json
[
  {
    "id": 1,
    "title": "Estudiar Spring",
    "description": "Revisar controller y service",
    "completed": false
  }
]
```

## Documentacion adicional

- Endpoints detallados: `api.md`
- Arquitectura: `architecture.md`
- Configuracion del entorno: `setup.md`

