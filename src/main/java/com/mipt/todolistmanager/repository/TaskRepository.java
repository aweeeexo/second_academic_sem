package com.mipt.todolistmanager.repository;

import com.mipt.todolistmanager.model.Task;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с задачами. Определяет базовые CRUD операции.
 *
 * @see InMemoryTaskRepository
 * @see StubTaskRepository
 */
public interface TaskRepository {

  /**
   * Сохраняет задачу (создает новую или обновляет существующую).
   *
   * @param task задача для сохранения
   * @return сохраненная задача
   */
  Task save(Task task);

  /**
   * Находит задачу по идентификатору.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей или пустой Optional
   */
  Optional<Task> findById(int id);

  /**
   * Возвращает все задачи.
   *
   * @return список всех задач
   */
  List<Task> findAll();

  /**
   * Удаляет задачу по идентификатору.
   *
   * @param id идентификатор задачи
   */
  void deleteById(int id);

  /**
   * Проверяет существование задачи по идентификатору.
   *
   * @param id идентификатор задачи
   * @return true если задача существует
   */
  boolean existsById(int id);
}