package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.model.Task;
import com.mipt.todolistmanager.service.TaskService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контроллер для управления задачами. Предоставляет CRUD операции для работы с задачами через
 * HTTP endpoints.
 *
 * @see TaskService
 * @see Task
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;

  /**
   * Конструктор с внедрением зависимости сервиса задач.
   *
   * @param taskService сервис для работы с задачами
   */
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  /**
   * Возвращает список всех задач. HTTP метод: GET /api/tasks
   *
   * @return ResponseEntity со списком всех задач и статусом 200 OK
   */
  @GetMapping
  public ResponseEntity<List<Task>> getAllTasks() {
    List<Task> tasks = taskService.findAll();
    return ResponseEntity.ok(tasks);
  }

  /**
   * Возвращает задачу по её идентификатору. HTTP метод: GET /api/tasks/{id}
   *
   * @param id идентификатор задачи
   * @return ResponseEntity с задачей и статусом 200 OK, или статусом 404 Not Found если задача не
   * найдена
   */
  @GetMapping("/api/tasks/{id}")
  public ResponseEntity<Task> getById(@PathVariable int id) {
    return taskService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Создает новую задачу. HTTP метод: POST /api/tasks
   *
   * @param task данные новой задачи
   * @return ResponseEntity с созданной задачей и статусом 201 Created
   */
  @PostMapping("/api/tasks")
  public ResponseEntity<Task> create(@RequestBody Task task) {
    Task saved = taskService.save(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  /**
   * Обновляет существующую задачу. HTTP метод: PUT /api/tasks/{id}
   *
   * @param id   идентификатор обновляемой задачи
   * @param task новые данные задачи
   * @return ResponseEntity с обновленной задачей и статусом 200 OK, или статусом 404 Not Found если
   * задача не найдена
   */
  @PutMapping("/api/tasks/{id}")
  public ResponseEntity<Task> update(@PathVariable int id, @RequestBody Task task) {
    if (!taskService.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    task.setId(id);
    Task updated = taskService.save(task);
    return ResponseEntity.ok(updated);
  }

  /**
   * Удаляет задачу по идентификатору. HTTP метод: DELETE /api/tasks/{id}
   *
   * @param id идентификатор удаляемой задачи
   * @return ResponseEntity со статусом 204 No Content если удаление успешно, или статусом 404 Not
   * Found если задача не найдена
   */
  @DeleteMapping("/api/tasks/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    if (!taskService.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    taskService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}