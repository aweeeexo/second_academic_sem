package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.dto.TaskCreateDto;
import com.mipt.todolistmanager.dto.TaskResponseDto;
import com.mipt.todolistmanager.dto.TaskUpdateDto;
import com.mipt.todolistmanager.service.TaskService;
import com.mipt.todolistmanager.validation.OnCreate;
import com.mipt.todolistmanager.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;

  @Value("${api.version}")
  private String apiVersion;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @Operation(summary = "Get all tasks")
  @GetMapping
  public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
    List<TaskResponseDto> tasks = taskService.findAll();
    return ResponseEntity.ok()
        .header("X-Total-Count", String.valueOf(taskService.getTotalCount()))
        .header("X-API-Version", apiVersion)
        .body(tasks);
  }

  @Operation(summary = "Get task by ID")
  @GetMapping("/{id}")
  public ResponseEntity<TaskResponseDto> getById(@PathVariable int id) {
    TaskResponseDto task = taskService.getTaskById((long) id);
    return ResponseEntity.ok()
        .header("X-API-Version", apiVersion)
        .body(task);
  }

  @Operation(summary = "Create a new task")
  @PostMapping
  public ResponseEntity<TaskResponseDto> create(@Validated(OnCreate.class) @RequestBody TaskCreateDto dto) {
    TaskResponseDto created = taskService.createTask(dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("X-API-Version", apiVersion)
        .body(created);
  }

  @Operation(summary = "Update an existing task")
  @PutMapping("/{id}")
  public ResponseEntity<TaskResponseDto> update(@PathVariable int id,
      @Validated(OnUpdate.class) @RequestBody TaskUpdateDto dto) {
    TaskResponseDto updated = taskService.updateTask((long) id, dto);
    return ResponseEntity.ok()
        .header("X-API-Version", apiVersion)
        .body(updated);
  }

  @Operation(summary = "Delete a task")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    taskService.deleteById((long) id);
    return ResponseEntity.noContent()
        .header("X-API-Version", apiVersion)
        .build();
  }
}