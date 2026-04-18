# Configuracion del entorno

Guia para preparar y ejecutar localmente el backend To-Do List.

## Requisitos

- Java instalado
  - Recomendado: Java 25 (coincide con `pom.xml` actual: `<java.version>25</java.version>`)
  - Si tu equipo usa Java 17, debes ajustar `pom.xml` a `17` para evitar incompatibilidades.
- Maven Wrapper incluido en el proyecto (`mvnw`, `mvnw.cmd`)
- IDE opcional: IntelliJ IDEA, VS Code, etc.

## Configuracion de `application.properties`

Archivo actual: `src/main/resources/application.properties`

Contenido minimo actual:

```properties
spring.application.name=backend
```

Como no hay propiedades de datasource definidas, Spring Boot aplicara su autoconfiguracion segun dependencias disponibles.

## Uso de variables de entorno para credenciales

Si deseas usar MySQL en lugar de una configuracion embebida/default, agrega propiedades con placeholders:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

En Windows PowerShell (sesion actual):

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/todolist"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="tu_password"
```

En persistencia de usuario (opcional):

```powershell
setx DB_URL "jdbc:mysql://localhost:3306/todolist"
setx DB_USERNAME "root"
setx DB_PASSWORD "tu_password"
```

> `setx` aplica para nuevas terminales, no para la ventana actual.

## Ejecutar proyecto localmente

### 1) Compilar

```powershell
.\mvnw.cmd clean compile
```

### 2) Ejecutar en modo desarrollo

```powershell
.\mvnw.cmd spring-boot:run
```

### 3) Probar endpoint principal

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/tasks"
```

## Comandos utiles con Maven Wrapper

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
java -jar .\target\backend-0.0.1-SNAPSHOT.jar
```

## Notas practicas

- Si ves errores con Lombok (por ejemplo, metodos `getTitle()` no detectados), valida:
  - Plugin Lombok habilitado en tu IDE.
  - Annotation Processing activado en el IDE.
  - Dependencia `lombok` presente (ya esta en `pom.xml`).
- Si cambias version de Java, limpia y recompila (`clean compile`).

