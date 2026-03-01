package com.mipt.todolistmanager.repository;

import com.mipt.todolistmanager.model.Task;
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
    return Optional.of(new Task(id, "StubTask", "description StubTask", true));
  }

  /**
   * Возвращает список предопределенных задач-заглушек.
   *
   * @return список из двух задач
   */
  @Override
  public List<Task> findall() {
    return List.of(
        new Task(-1, "StubTask -1", "description StubTask for findAll -1", false),
        new Task(-2, "StubTask -2", "description StubTask for findAll -2", true)
    );
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
  public boolean existById(int id) {
    return false;
  }
}