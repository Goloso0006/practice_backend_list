# Guia paso a paso: migrar a PostgreSQL (Render)

Esta guia te explica como pasar tu backend Spring Boot de H2 en memoria a PostgreSQL en Render, manteniendo tu frontend en Vercel.

## Objetivo

- Dejar de guardar datos en memoria (H2) para tener persistencia real.
- Mantener el flujo actual del proyecto con el menor cambio posible.
- Desplegar en Render usando variables de entorno.

## Estado actual del proyecto (confirmado)

Actualmente en `src/main/resources/application.properties` tienes H2 en memoria:

- `spring.datasource.url=jdbc:h2:mem:todolist`
- `spring.jpa.database-platform=org.hibernate.dialect.H2Dialect`

Eso significa que los datos no son ideales para produccion (pueden perderse en reinicios/redeploy).

---

## Checklist rapido

- [ ] Crear base PostgreSQL en Render
- [ ] Agregar driver PostgreSQL en `pom.xml`
- [ ] Ajustar `application.properties` para usar variables de entorno
- [ ] Configurar variables en Render
- [ ] Hacer deploy
- [ ] Validar CRUD desde backend y frontend

---

## 1) Crear PostgreSQL en Render

1. Entra a Render Dashboard.
2. Click en **New +** -> **PostgreSQL**.
3. Define nombre, region y plan.
4. Espera a que la DB quede en estado **Available**.
5. Copia estos datos desde la DB:
   - Host : dpg-d7iqt9t8nd3s738leck0-a
   - Port: 5432
   - Database: task_list_0hyi
   - User: task_list_0hyi_user
   - Password: UycGEd1QchNQUK2j4nkPZAIwoaelMraC
   - (Opcional) Connection String

> Recomendacion: usa la misma region que tu backend para menor latencia.

---

## 2) Actualizar dependencias en `pom.xml`

En tu `pom.xml`, agrega el driver de PostgreSQL:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Nota sobre dependencias actuales

- Puedes conservar H2 para desarrollo local.
- Si no usas MySQL, puedes quitar:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## 3) Configurar `application.properties` para PostgreSQL

Edita `src/main/resources/application.properties` para usar variables de entorno.

Ejemplo recomendado:

```properties
spring.application.name=backend

# Datasource (produccion)
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Opcional (desarrollo local con fallback)

Si quieres correr local sin Render, puedes usar fallback:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:h2:mem:todolist}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:sa}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}
spring.datasource.driver-class-name=${SPRING_DATASOURCE_DRIVER:org.h2.Driver}
spring.jpa.database-platform=${SPRING_JPA_DIALECT:org.hibernate.dialect.H2Dialect}
```

---

## 4) Configurar variables de entorno en Render (servicio backend)

En tu servicio backend de Render, ve a **Environment** y agrega:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME` 
- `SPRING_DATASOURCE_PASSWORD` 

Formato tipico de URL JDBC para PostgreSQL:

```text
jdbc:postgresql://<HOST>:<PORT>/<DATABASE>
```

Ejemplo:

```text
jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/todolist_db
```

> Si Render te da una URL tipo `postgres://...`, convierte a `jdbc:postgresql://...` para Spring Boot.

---

## 5) Deploy en Render

1. Guarda cambios en tu repo.
2. Push a la rama conectada con Render.
3. Verifica logs de arranque del backend.

Debes ver que inicia sin errores de conexion y Hibernate levanta con dialecto PostgreSQL.

---

## 6) Verificacion funcional

### Verifica endpoints del backend

Prueba desde Postman/Insomnia o PowerShell:

```powershell
Invoke-RestMethod -Method Get -Uri "https://TU_BACKEND.onrender.com/tasks"
```

Crear una tarea:

```powershell
$body = @{ title = "Tarea test"; description = "Prueba postgres"; completed = $false } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "https://TU_BACKEND.onrender.com/tasks" -ContentType "application/json" -Body $body
```

Luego:

- Reinicia el servicio en Render.
- Vuelve a consultar `/tasks`.
- Si la tarea sigue ahi, ya tienes persistencia real.

---

## 7) Troubleshooting rapido

### Error de autenticacion

- Revisa `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD`.
- Confirma que coincidan exactamente con los valores de la DB.

### Error de URL

- Verifica que empiece por `jdbc:postgresql://`.
- Confirma host, puerto y nombre de DB.

### Error de CORS (frontend no conecta)

- Revisa `src/main/java/com/todolist/backend/config/CorsConfig.java`.
- Asegura que tu dominio de Vercel este en `allowedOrigins(...)`.

### Tabla no creada

- Con `spring.jpa.hibernate.ddl-auto=update` deberia crear/actualizar tablas.
- Revisa logs por errores SQL o permisos.

---

## 8) Rollback basico (si algo falla)

1. Quita variables PostgreSQL del servicio (o restaura las anteriores).
2. Vuelve a configuracion H2 temporal en `application.properties`.
3. Redeploy.

Configuracion H2 temporal:

```properties
spring.datasource.url=jdbc:h2:mem:todolist
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

---

## Recomendacion final

Para tu caso (backend en Render + frontend en Vercel), la opcion mas simple y estable es:

- **PostgreSQL gestionado en Render**

Asi reduces complejidad operativa y mantienes todo el backend en la misma plataforma.

