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

  @Override
  public Task save(Task task) {
    if (task.getId() == 0) {
      task.setId((long) currentId++);
    }
    tasks.put(Math.toIntExact(task.getId()), task);
    return task;
  }

  @Override
  public Optional<Task> findById(int id) {
    return Optional.ofNullable(tasks.get(id));
  }

  @Override
  public List<Task> findAll() {
    return new ArrayList<>(tasks.values());
  }

  @Override
  public void deleteById(int id) {
    tasks.remove(id);
  }

  @Override
  public boolean existsById(int id) {
    return tasks.containsKey(id);
  }

  public Map<Integer, Task> findAllAsMap() {
    return new HashMap<>(tasks);
  }
}