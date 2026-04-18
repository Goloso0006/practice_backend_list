package com.todolist.backend.service;

import com.todolist.backend.model.Task;
import com.todolist.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTaskShouldSaveWhenTitleIsValid() {
        Task task = new Task(null, "Aprender CI", "Practicar tests", false);
        Task savedTask = new Task(1L, "Aprender CI", "Practicar tests", false);

        when(taskRepository.save(task)).thenReturn(savedTask);

        Task result = taskService.createTask(task);

        assertEquals(1L, result.getId());
        assertEquals("Aprender CI", result.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void createTaskShouldThrowWhenTitleIsBlank() {
        Task task = new Task(null, "   ", "Sin titulo", false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.createTask(task));

        assertEquals("El título no puede estar vacío", ex.getMessage());
    }

    @Test
    void getAllTasksShouldReturnRepositoryData() {
        when(taskRepository.findAll()).thenReturn(List.of(
                new Task(1L, "T1", "D1", false),
                new Task(2L, "T2", "D2", true)
        ));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void updateTaskShouldUpdateExistingTask() {
        Task existing = new Task(1L, "Viejo", "Desc vieja", false);
        Task incoming = new Task(null, "Nuevo", "Desc nueva", true);
        Task updated = new Task(1L, "Nuevo", "Desc nueva", true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenReturn(updated);

        Task result = taskService.updateTask(1L, incoming);

        assertEquals("Nuevo", result.getTitle());
        assertEquals(true, result.getCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existing);
    }

    @Test
    void updateTaskShouldThrowWhenTaskDoesNotExist() {
        Task incoming = new Task(null, "Nuevo", "Desc", true);
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.updateTask(999L, incoming));

        assertEquals("Tarea no encontrada", ex.getMessage());
    }

    @Test
    void deleteTaskShouldCallRepository() {
        taskService.deleteTask(10L);

        verify(taskRepository, times(1)).deleteById(10L);
    }
}

