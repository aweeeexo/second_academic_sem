package com.example.securegateway.external;

import com.example.securegateway.dto.TaskDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/external/v1")
public class ExternalApiController {

  private final ConcurrentMap<Long, TaskDto> storage = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @PostMapping("/tasks")
  public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task) {
    long id = idGenerator.getAndIncrement();
    task.setId(id);
    storage.put(id, task);
    return ResponseEntity.created(URI.create("/external/v1/tasks/" + id)).body(task);
  }

  @GetMapping("/tasks/{id}")
  public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
    TaskDto task = storage.get(id);
    if (task == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(task);
  }

  @GetMapping("/tasks")
  public ResponseEntity<List<TaskDto>> getTasks(@RequestParam(required = false) Boolean completed,
      @RequestParam(required = false) Integer limit) {
    List<TaskDto> result = new ArrayList<>(storage.values());
    if (completed != null) {
      result = result.stream().filter(t -> t.getCompleted().equals(completed)).toList();
    }
    if (limit != null && limit > 0) {
      result = result.stream().limit(limit).toList();
    }
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/tasks/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    if (storage.remove(id) == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/unstable")
  public ResponseEntity<?> unstable(@RequestParam String mode) throws InterruptedException {
    if ("timeout".equals(mode)) {
      Thread.sleep(10_000);
      return ResponseEntity.ok("after timeout");
    } else if ("500".equals(mode)) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
    } else if ("429".equals(mode)) {
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
          .header("Retry-After", "30")
          .body("Rate limit exceeded");
    } else if ("html".equals(mode)) {
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
          .contentType(org.springframework.http.MediaType.TEXT_HTML)
          .body("<html><body>Gateway Error</body></html>");
    }
    return ResponseEntity.badRequest().body("Unknown mode");
  }
}