package com.mipt.todolistmanager.repository;

import com.mipt.todolistmanager.model.Priority;
import com.mipt.todolistmanager.model.Task;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий-заглушка с фиксированными тестовыми данными. Используется для демонстрации и
 * тестирования. Не сохраняет изменения, всегда возвращает предопределенные задачи.
 *
 * @see TaskRepository
 */
public class StubTaskRepository implements TaskRepository {

  /**
   * "Сохраняет" задачу (фактически просто возвращает её без сохранения).
   *
   * @param task задача для сохранения
   * @return та же задача без изменений
   */
  @Override
  public Task save(Task task) {
    return task;
  }

  /**
   * Возвращает заглушку задачи по ID.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей-заглушкой
   */
  @Override
  public Optional<Task> findById(int id) {
    Task task = new Task();
    task.setId((long) id);
    task.setTitle("StubTask");
    task.setDescription("description StubTask");
    task.setCompleted(true);
    task.setCreatedAt(LocalDateTime.now());
    task.setDueDate(LocalDate.now().plusDays(7));
    task.setPriority(Priority.MEDIUM);
    task.setTags(new HashSet<>());
    return Optional.of(task);
  }

  /**
   * Возвращает список предопределенных задач-заглушек.
   *
   * @return список из двух задач
   */
  @Override
  public List<Task> findAll() {
    Task task1 = new Task();
    task1.setId((long) -1);
    task1.setTitle("StubTask -1");
    task1.setDescription("description StubTask for findAll -1");
    task1.setCompleted(false);
    task1.setCreatedAt(LocalDateTime.now().minusDays(5));
    task1.setDueDate(LocalDate.now().plusDays(2));
    task1.setPriority(Priority.LOW);
    task1.setTags(new HashSet<>());

    Task task2 = new Task();
    task2.setId((long) -2);
    task2.setTitle("StubTask -2");
    task2.setDescription("description StubTask for findAll -2");
    task2.setCompleted(true);
    task2.setCreatedAt(LocalDateTime.now().minusDays(10));
    task2.setDueDate(LocalDate.now().minusDays(1));
    task2.setPriority(Priority.HIGH);
    task2.setTags(new HashSet<>());

    return List.of(task1, task2);
  }

  /**
   * Заглушка метода удаления (не выполняет действий).
   *
   * @param id идентификатор задачи
   */
  @Override
  public void deleteById(int id) {
  }

  /**
   * Всегда возвращает false, так как заглушка не хранит реальные задачи.
   *
   * @param id идентификатор задачи
   * @return false
   */
  @Override
  public boolean existsById(int id) {
    return false;
  }
}