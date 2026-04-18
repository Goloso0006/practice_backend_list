package com.todolist.backend.repository;

// Importamos la entidad Task (tabla que vamos a manejar)
import com.todolist.backend.model.Task;

// Importamos JpaRepository: Spring genera CRUD automático
import org.springframework.data.jpa.repository.JpaRepository;

// REPOSITORY = capa acceso datos
// Extiende JpaRepository para heredar métodos CRUD listos
public interface TaskRepository extends JpaRepository<Task, Long> {

// No escribimos métodos manualmente porque JpaRepository ya incluye:

    // save(task)        -> guardar o actualizar tarea
    // findAll()         -> listar todas las tareas
    // findById(id)      -> buscar una tarea por ID
    // deleteById(id)    -> eliminar una tarea por ID
}
