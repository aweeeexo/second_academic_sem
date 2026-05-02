package com.example.securegateway.api;

import com.example.securegateway.dto.TaskDto;
import com.example.securegateway.service.TasksGatewayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TasksController {

  private final TasksGatewayService gatewayService;

  public TasksController(TasksGatewayService gatewayService) {
    this.gatewayService = gatewayService;
  }

  @PostMapping
  public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task) {
    TaskDto created = gatewayService.createTask(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
    return ResponseEntity.ok(gatewayService.getTask(id));
  }

  @GetMapping
  public ResponseEntity<List<TaskDto>> getTasks(@RequestParam(required = false) Boolean completed,
      @RequestParam(required = false) Integer limit) {
    return ResponseEntity.ok(gatewayService.getTasks(completed, limit));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    gatewayService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }
}