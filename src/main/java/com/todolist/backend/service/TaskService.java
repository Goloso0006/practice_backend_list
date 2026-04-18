package com.todolist.backend.service;
import com.todolist.backend.model.Task;
import com.todolist.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    // Inyección de dependencia por constructor
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {  // Crear tarea

        // Validación lógica negocio
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new RuntimeException("El título no puede estar vacío");
        }
        return taskRepository.save(task); // Guardar en BD
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task updateTask(Long id, Task task) {

        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setCompleted(task.getCompleted());

        return taskRepository.save(existingTask);
    }

}