package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.model.Task;
import com.mipt.todolistmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockitoBean
  private TaskService taskService;

  private Task testTask1;
  private Task testTask2;
  private Task newTask;

  @BeforeEach
  void setUp() {
    testTask1 = new Task();
    testTask1.setId(1);
    testTask1.setTitle("Купить продукты");
    testTask1.setDescription("Молоко, хлеб, яйца");
    testTask1.setCompleted(false);

    testTask2 = new Task();
    testTask2.setId(2);
    testTask2.setTitle("Сделать домашку");
    testTask2.setDescription("Spring Boot задание");
    testTask2.setCompleted(false);

    newTask = new Task();
    newTask.setTitle("Новая задача");
    newTask.setDescription("Описание новой задачи");
    newTask.setCompleted(false);

    reset(taskService);
  }

  @Test
  void getAllTasks_ShouldReturnListOfTasks() {
    when(taskService.findAll()).thenReturn(List.of(testTask1, testTask2));

    ResponseEntity<Task[]> response = restTemplate.getForEntity("/api/tasks", Task[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).hasSize(2);
    assertThat(response.getBody()[0].getId()).isEqualTo(1);
    assertThat(response.getBody()[1].getId()).isEqualTo(2);

    verify(taskService, times(1)).findAll();
  }

  @Test
  void getAllTasks_WhenNoTasks_ShouldReturnEmptyList() {
    when(taskService.findAll()).thenReturn(List.of());

    ResponseEntity<Task[]> response = restTemplate.getForEntity("/api/tasks", Task[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).isEmpty();

    verify(taskService, times(1)).findAll();
  }

  @Test
  void getTaskById_WithValidId_ShouldReturnTask() {
    when(taskService.findById(1)).thenReturn(Optional.of(testTask1));

    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/1", Task.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isEqualTo(1);

    verify(taskService, times(1)).findById(1);
  }

  @Test
  void getTaskById_WithInvalidId_ShouldReturn404() {
    when(taskService.findById(999)).thenReturn(Optional.empty());

    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/999", Task.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNull();

    verify(taskService, times(1)).findById(999);
  }

  @Test
  void getTaskById_WithInvalidIdFormat_ShouldReturn400() {
    ResponseEntity<String> response = restTemplate.getForEntity("/api/tasks/abc", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    verify(taskService, never()).findById(anyInt());
  }

  @Test
  void createTask_WithValidData_ShouldReturnCreatedTask() {
    Task savedTask = new Task();
    savedTask.setId(3);
    savedTask.setTitle(newTask.getTitle());
    savedTask.setDescription(newTask.getDescription());
    savedTask.setCompleted(false);

    when(taskService.save(any(Task.class))).thenReturn(savedTask);

    ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", newTask, Task.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isEqualTo(3);

    verify(taskService, times(1)).save(any(Task.class));
  }

  @Test
  void createTask_WithEmptyTitle_ShouldReturn400() {
    Task invalidTask = new Task();
    invalidTask.setTitle("");
    invalidTask.setDescription("Описание");
    invalidTask.setCompleted(false);

    when(taskService.save(any(Task.class)))
        .thenThrow(new IllegalArgumentException("Task title cannot be empty"));

    ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", invalidTask, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    verify(taskService, times(1)).save(any(Task.class));
  }

  @Test
  void createTask_WithNullTitle_ShouldReturn400() {
    Task invalidTask = new Task();
    invalidTask.setTitle(null);
    invalidTask.setDescription("Описание");
    invalidTask.setCompleted(false);

    when(taskService.save(any(Task.class)))
        .thenThrow(new IllegalArgumentException("Task title cannot be empty"));

    ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", invalidTask, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    verify(taskService, times(1)).save(any(Task.class));
  }

  @Test
  void updateTask_WithValidIdAndData_ShouldReturnUpdatedTask() {
    Task updatedTask = new Task();
    updatedTask.setId(1);
    updatedTask.setTitle("Обновленный заголовок");
    updatedTask.setDescription("Обновленное описание");
    updatedTask.setCompleted(true);

    when(taskService.existsById(1)).thenReturn(true);
    when(taskService.save(any(Task.class))).thenReturn(updatedTask);

    HttpEntity<Task> requestEntity = new HttpEntity<>(updatedTask);
    ResponseEntity<Task> response = restTemplate.exchange(
        "/api/tasks/1",
        HttpMethod.PUT,
        requestEntity,
        Task.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isEqualTo(1);
    assertThat(response.getBody().getTitle()).isEqualTo("Обновленный заголовок");

    verify(taskService, times(1)).existsById(1);
    verify(taskService, times(1)).save(any(Task.class));
  }

  @Test
  void updateTask_WithInvalidId_ShouldReturn404() {
    Task updatedTask = new Task();
    updatedTask.setId(999);
    updatedTask.setTitle("Обновленный заголовок");
    updatedTask.setDescription("Описание");
    updatedTask.setCompleted(true);

    when(taskService.existsById(999)).thenReturn(false);

    HttpEntity<Task> requestEntity = new HttpEntity<>(updatedTask);
    ResponseEntity<String> response = restTemplate.exchange(
        "/api/tasks/999",
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    verify(taskService, times(1)).existsById(999);
    verify(taskService, never()).save(any(Task.class));
  }

  @Test
  void updateTask_WithInvalidIdFormat_ShouldReturn400() {
    Task updatedTask = new Task();
    updatedTask.setId(1);
    updatedTask.setTitle("Заголовок");
    updatedTask.setDescription("Описание");
    updatedTask.setCompleted(true);

    HttpEntity<Task> requestEntity = new HttpEntity<>(updatedTask);
    ResponseEntity<String> response = restTemplate.exchange(
        "/api/tasks/abc",
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    verify(taskService, never()).existsById(anyInt());
    verify(taskService, never()).save(any(Task.class));
  }

  @Test
  void deleteTask_WithValidId_ShouldReturn204() {
    when(taskService.existsById(1)).thenReturn(true);
    doNothing().when(taskService).deleteById(1);

    ResponseEntity<Void> response = restTemplate.exchange(
        "/api/tasks/1",
        HttpMethod.DELETE,
        null,
        Void.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    verify(taskService, times(1)).existsById(1);
    verify(taskService, times(1)).deleteById(1);
  }

  @Test
  void deleteTask_WithInvalidId_ShouldReturn404() {
    when(taskService.existsById(999)).thenReturn(false);

    ResponseEntity<Void> response = restTemplate.exchange(
        "/api/tasks/999",
        HttpMethod.DELETE,
        null,
        Void.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    verify(taskService, times(1)).existsById(999);
    verify(taskService, never()).deleteById(anyInt());
  }

  @Test
  void deleteTask_WithInvalidIdFormat_ShouldReturn400() {
    ResponseEntity<String> response = restTemplate.exchange(
        "/api/tasks/abc",
        HttpMethod.DELETE,
        null,
        String.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    verify(taskService, never()).existsById(anyInt());
    verify(taskService, never()).deleteById(anyInt());
  }
}