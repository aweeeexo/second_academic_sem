package com.mipt.todolistmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.todolistmanager.dto.TaskCreateDto;
import com.mipt.todolistmanager.dto.TaskResponseDto;
import com.mipt.todolistmanager.dto.TaskUpdateDto;
import com.mipt.todolistmanager.model.Priority;
import com.mipt.todolistmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TaskService taskService;

  private TaskCreateDto createDto;
  private TaskUpdateDto updateDto;
  private TaskResponseDto responseDto;

  @BeforeEach
  void setUp() {
    createDto = new TaskCreateDto();
    createDto.setTitle("Test Task");
    createDto.setDescription("Test Description");
    createDto.setDueDate(LocalDate.now().plusDays(1));
    createDto.setPriority(Priority.MEDIUM);
    createDto.setTags(new HashSet<>(Arrays.asList("work", "important")));

    updateDto = new TaskUpdateDto();
    updateDto.setTitle("Updated Task");
    updateDto.setCompleted(true);

    responseDto = new TaskResponseDto();
    responseDto.setId(1L);
    responseDto.setTitle("Test Task");
    responseDto.setDescription("Test Description");
    responseDto.setCompleted(false);
    responseDto.setCreatedAt(LocalDateTime.now());
    responseDto.setDueDate(LocalDate.now().plusDays(1));
    responseDto.setPriority(Priority.MEDIUM);
    responseDto.setTags(new HashSet<>(Arrays.asList("work", "important")));
  }

  @Test
  void getAllTasks_ShouldReturnListOfTasks() throws Exception {
    List<TaskResponseDto> tasks = Arrays.asList(responseDto);
    when(taskService.findAll()).thenReturn(tasks);
    when(taskService.getTotalCount()).thenReturn(1);

    mockMvc.perform(get("/api/tasks"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-Total-Count", "1"))
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].title").value("Test Task"));
  }

  @Test
  void getTaskById_WhenTaskExists_ShouldReturnTask() throws Exception {
    when(taskService.getTaskById(1L)).thenReturn(responseDto);

    mockMvc.perform(get("/api/tasks/1"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Test Task"));
  }

  @Test
  void getTaskById_WhenTaskNotFound_ShouldReturn404() throws Exception {
    when(taskService.getTaskById(999L)).thenThrow(new RuntimeException("Task not found"));

    mockMvc.perform(get("/api/tasks/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createTask_WithValidData_ShouldReturn201() throws Exception {
    when(taskService.createTask(any(TaskCreateDto.class))).thenReturn(responseDto);

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isCreated())
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Test Task"));
  }

  @Test
  void createTask_WithEmptyTitle_ShouldReturn400() throws Exception {
    createDto.setTitle("");

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createTask_WithTitleTooShort_ShouldReturn400() throws Exception {
    createDto.setTitle("ab");

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createTask_WithNullPriority_ShouldReturn400() throws Exception {
    createDto.setPriority(null);

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createTask_WithDueDateInPast_ShouldReturn400() throws Exception {
    createDto.setDueDate(LocalDate.now().minusDays(1));

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createTask_WithTooManyTags_ShouldReturn400() throws Exception {
    HashSet<String> tags = new HashSet<>();
    for (int i = 0; i < 6; i++) {
      tags.add("tag" + i);
    }
    createDto.setTags(tags);

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateTask_WithValidData_ShouldReturn200() throws Exception {
    when(taskService.updateTask((long) eq(1), any(TaskUpdateDto.class))).thenReturn(responseDto);

    mockMvc.perform(put("/api/tasks/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDto)))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"));
  }

  @Test
  void updateTask_WhenTaskNotFound_ShouldReturn404() throws Exception {
    when(taskService.updateTask((long) eq(999), any(TaskUpdateDto.class)))
        .thenThrow(new RuntimeException("Task not found"));

    mockMvc.perform(put("/api/tasks/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDto)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteTask_WhenTaskExists_ShouldReturn204() throws Exception {
    doNothing().when(taskService).deleteById(1L);

    mockMvc.perform(delete("/api/tasks/1"))
        .andExpect(status().isNoContent())
        .andExpect(header().string("X-API-Version", "2.0.0"));
  }
}