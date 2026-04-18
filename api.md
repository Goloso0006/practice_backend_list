# API REST - To-Do List

Base URL local:

`http://localhost:8080`

Entidad usada en requests/responses:

```json
{
  "id": 1,
  "title": "Texto",
  "description": "Texto",
  "completed": false
}
```

> Nota: En el codigo actual no hay `@ControllerAdvice` para mapear errores, por lo que validaciones y errores de negocio lanzan `RuntimeException` y suelen devolverse como `500 Internal Server Error`.

---

## 1) GET /tasks

Obtiene todas las tareas.

- Metodo: `GET`
- Ruta: `/tasks`
- Parametros: No aplica

### Request de ejemplo

```http
GET /tasks
```

### Response de ejemplo (`200 OK`)

```json
[
  {
    "id": 1,
    "title": "Comprar pan",
    "description": "Ir a la tienda",
    "completed": false
  },
  {
    "id": 2,
    "title": "Enviar reporte",
    "description": "Antes de las 6 pm",
    "completed": true
  }
]
```

---

## 2) POST /tasks

Crea una nueva tarea.

- Metodo: `POST`
- Ruta: `/tasks`
- Parametros: No aplica
- Body JSON requerido: `title`, `description`, `completed`

### Request de ejemplo

```http
POST /tasks
Content-Type: application/json

{
  "title": "Estudiar JPA",
  "description": "Revisar repositorios",
  "completed": false
}
```

### Response de ejemplo (`200 OK` en implementacion actual)

```json
{
  "id": 3,
  "title": "Estudiar JPA",
  "description": "Revisar repositorios",
  "completed": false
}
```

### Error de validacion (implementacion actual)

Si `title` llega vacio o en blanco:

```json
{
  "timestamp": "2026-04-11T17:45:10.111+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/tasks"
}
```

Mensaje interno lanzado por servicio: `El titulo no puede estar vacio`.

---

## 3) PUT /tasks/{id}

Actualiza una tarea existente por ID.

- Metodo: `PUT`
- Ruta: `/tasks/{id}`
- Parametros de ruta:
  - `id` (Long): identificador de la tarea

### Request de ejemplo

```http
PUT /tasks/3
Content-Type: application/json

{
  "title": "Estudiar JPA y Hibernate",
  "description": "Agregar pruebas",
  "completed": true
}
```

### Response de ejemplo (`200 OK`)

```json
{
  "id": 3,
  "title": "Estudiar JPA y Hibernate",
  "description": "Agregar pruebas",
  "completed": true
}
```

### Error (ID no encontrado, implementacion actual)

Cuando no existe la tarea:

```json
{
  "timestamp": "2026-04-11T17:50:00.001+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/tasks/999"
}
```

Mensaje interno lanzado por servicio: `Tarea no encontrada`.

---

## 4) DELETE /tasks/{id}

Elimina una tarea por ID.

- Metodo: `DELETE`
- Ruta: `/tasks/{id}`
- Parametros de ruta:
  - `id` (Long): identificador de la tarea

### Request de ejemplo

```http
DELETE /tasks/3
```

### Response de ejemplo (`200 OK`)

Sin cuerpo de respuesta.

> Dependiendo del ID y estado de persistencia, la excepcion de borrado puede aparecer como error generico si no se controla explicitamente.

