package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.model.Task;
import com.mipt.todolistmanager.service.TaskService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public ResponseEntity<List<Task>> getAllTasks() {
    List<Task> tasks = taskService.findAll();
    return ResponseEntity.ok(tasks);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Task> getById(@PathVariable int id) {
    return taskService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Task> create(@RequestBody Task task) {
    Task saved = taskService.save(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> update(@PathVariable int id, @RequestBody Task task) {
    if (!taskService.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    task.setId(id);
    Task updated = taskService.save(task);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    if (!taskService.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    taskService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}