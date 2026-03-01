package com.mipt.todolistmanager.repository;

import com.mipt.todolistmanager.model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * Реализация репозитория задач в оперативной памяти. Хранит задачи в HashMap и генерирует ID
 * автоматически. Является основным (primary) репозиторием.
 *
 * @see TaskRepository
 */
@Repository
@Primary
public class InMemoryTaskRepository implements TaskRepository {

  private final Map<Integer, Task> tasks = new HashMap<>();
  private int currentId = 1;

  /**
   * Сохраняет задачу. Если ID задачи равен 0, генерирует новый ID.
   *
   * @param task задача для сохранения
   * @return сохраненная задача с установленным ID
   */
  @Override
  public Task save(Task task) {
    if (task.getId() == 0) {
      task.setId(currentId++);
    }
    tasks.put(task.getId(), task);
    return task;
  }

  /**
   * Находит задачу по ID.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей или пустой Optional
   */
  @Override
  public Optional<Task> findById(int id) {
    return Optional.ofNullable(tasks.get(id));
  }

  /**
   * Возвращает все задачи.
   *
   * @return список всех задач
   */
  @Override
  public List<Task> findall() {
    return new ArrayList<>(tasks.values());
  }

  /**
   * Удаляет задачу по ID.
   *
   * @param id идентификатор задачи
   */
  @Override
  public void deleteById(int id) {
    tasks.remove(id);
  }

  /**
   * Проверяет существование задачи по ID.
   *
   * @param id идентификатор задачи
   * @return true если задача существует
   */
  @Override
  public boolean existById(int id) {
    return tasks.containsKey(id);
  }
}