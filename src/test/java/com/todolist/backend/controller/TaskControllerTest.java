package com.todolist.backend.controller;

import com.todolist.backend.model.Task;
import com.todolist.backend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TaskController taskController = new TaskController(taskService);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void getAllTasksShouldReturnTaskList() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(
                new Task(1L, "T1", "D1", false),
                new Task(2L, "T2", "D2", true)
        ));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("T1"))
                .andExpect(jsonPath("$[1].completed").value(true));
    }

    @Test
    void createTaskShouldReturnCreatedTask() throws Exception {
        Task created = new Task(1L, "Crear", "Nueva tarea", false);
        String requestBody = """
                {
                  "title": "Crear",
                  "description": "Nueva tarea",
                  "completed": false
                }
                """;

        when(taskService.createTask(org.mockito.ArgumentMatchers.any(Task.class))).thenReturn(created);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Crear"));

        verify(taskService, times(1)).createTask(org.mockito.ArgumentMatchers.any(Task.class));
    }

    @Test
    void deleteTaskShouldCallService() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 7L))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(7L);
    }
}
