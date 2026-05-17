package com.mipt.todolistmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.todolistmanager.dto.TaskCreateDto;
import com.mipt.todolistmanager.dto.TaskResponseDto;
import com.mipt.todolistmanager.model.Priority;
import com.mipt.todolistmanager.service.TaskService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskService taskService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createTask_ShouldReturn201() throws Exception {
    TaskCreateDto createDto = new TaskCreateDto();
    createDto.setTitle("Test Task");
    createDto.setDescription("Description");
    createDto.setPriority(Priority.HIGH);
    createDto.setDueDate(LocalDate.now().plusDays(1));

    TaskResponseDto responseDto = new TaskResponseDto();
    responseDto.setId(1L);
    responseDto.setTitle("Test Task");
    responseDto.setCompleted(false);

    when(taskService.createTask(any(TaskCreateDto.class))).thenReturn(responseDto);

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.title").value("Test Task"));
  }

  @Test
  void getTaskById_WhenExists_ShouldReturn200() throws Exception {
    Long taskId = 1L;
    TaskResponseDto responseDto = new TaskResponseDto();
    responseDto.setId(taskId);
    responseDto.setTitle("Existing Task");

    when(taskService.getTaskById(taskId)).thenReturn(responseDto);

    mockMvc.perform(get("/api/tasks/{id}", taskId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(taskId))
        .andExpect(jsonPath("$.title").value("Existing Task"));
  }
  @Test
  void createTask_WithInvalidData_ShouldReturn400() throws Exception {
    TaskCreateDto invalidDto = new TaskCreateDto();
    invalidDto.setTitle("");
    invalidDto.setPriority(null);

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest());
  }
}