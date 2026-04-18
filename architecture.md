# Arquitectura del proyecto

Este backend usa una arquitectura en capas para separar responsabilidades y mantener el codigo simple de mantener.

## Patron en capas

### 1. Controller (`TaskController`)

- Recibe peticiones HTTP (`GET`, `POST`, `PUT`, `DELETE`).
- Convierte JSON de entrada a objetos `Task`.
- Delega la logica al servicio.

### 2. Service (`TaskService`)

- Contiene reglas de negocio.
- Valida datos (por ejemplo, `title` no vacio en `createTask`).
- Coordina operaciones de lectura/escritura con el repositorio.

### 3. Repository (`TaskRepository`)

- Capa de acceso a datos.
- Extiende `JpaRepository<Task, Long>` para CRUD sin codigo SQL manual.

### 4. Model (`Task`)

- Entidad JPA anotada con `@Entity`.
- Representa la tabla de tareas.
- Define campos persistentes: `id`, `title`, `description`, `completed`.

## Flujo de datos

Flujo principal de una operacion:

`Cliente -> Controller -> Service -> Repository -> Base de datos`

Ejemplo con `POST /tasks`:

1. El cliente envia JSON de una tarea.
2. `TaskController` recibe el body y llama a `taskService.createTask(task)`.
3. `TaskService` valida el titulo y delega en `taskRepository.save(task)`.
4. `TaskRepository` persiste usando JPA/Hibernate.
5. El resultado vuelve por las capas hasta el cliente como JSON.

## Diagrama simple (Markdown)

```text
+---------+      HTTP      +-----------------+      Java      +----------------+
| Cliente |  ------------> | TaskController  | -------------> |  TaskService   |
+---------+                +-----------------+                +----------------+
      ^                              |                                  |
      |                              |                                  v
      |                         JSON <-> Task                    +------------------+
      |                                                          | TaskRepository   |
      |                                                          | JpaRepository    |
      |                                                          +------------------+
      |                                                                   |
      +--------------------------- JSON Response --------------------------+
                                                                          v
                                                                   +-------------+
                                                                   |   DB (JPA)  |
                                                                   +-------------+
```

## Uso de JPA y ORM

- JPA permite mapear clases Java a tablas relacionales con anotaciones.
- En `Task`:
  - `@Entity`: marca la clase como entidad persistente.
  - `@Id`: define la clave primaria.
  - `@GeneratedValue(strategy = GenerationType.IDENTITY)`: ID autoincremental.
- `JpaRepository` abstrae operaciones CRUD comunes (`save`, `findAll`, `findById`, `deleteById`).
- El ORM (normalmente Hibernate en Spring Boot) traduce operaciones Java a SQL para la base de datos configurada.

## Ventajas de esta estructura

- Menor acoplamiento entre HTTP, logica de negocio y persistencia.
- Facilita pruebas por capa.
- Escala mejor cuando crecen reglas de negocio y numero de endpoints.

