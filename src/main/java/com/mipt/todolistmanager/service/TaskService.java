package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.model.Task;
import com.mipt.todolistmanager.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления задачами. Содержит бизнес-логику приложения и делегирует операции
 * репозиторию. Демонстрирует жизненный цикл бина через аннотации {@link PostConstruct} и
 * {@link PreDestroy}.
 *
 * <p>Особенности:
 * <ul>
 *   <li>Инициализация кэша при старте приложения через @PostConstruct</li>
 *   <li>Очистка ресурсов перед уничтожением бина через @PreDestroy</li>
 *   <li>Валидация входных данных (проверка заголовка задачи)</li>
 *   <li>Делегирование CRUD операций репозиторию</li>
 * </ul>
 *
 * @see TaskRepository
 * @see Task
 */
@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final Map<String, Task> taskCache = new HashMap<>();

  /**
   * Конструктор с внедрением зависимости репозитория.
   *
   * @param taskRepository репозиторий для работы с задачами
   */
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  /**
   * Возвращает список всех задач. Если задач нет, выводит информационное сообщение.
   *
   * @return список всех задач
   */
  public List<Task> findAll() {
    if (taskRepository.findall().isEmpty()) {
      System.out.println("Tasks are not exists yet");
    }
    return taskRepository.findall();
  }

  /**
   * Находит задачу по идентификатору.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей или пустой Optional, если задача не найдена
   */
  public Optional<Task> findById(int id) {
    return taskRepository.findById(id);
  }

  /**
   * Сохраняет задачу (создает новую или обновляет существующую). Выполняет валидацию: заголовок
   * задачи не может быть null.
   *
   * @param task задача для сохранения
   * @return сохраненная задача
   * @throws IllegalArgumentException если заголовок задачи равен null
   */
  public Task save(Task task) {
    if (task.getTitle() == null) {
      throw new IllegalArgumentException("Task title cannot be empty");
    } else {
      return taskRepository.save(task);
    }
  }

  /**
   * Удаляет задачу по идентификатору.
   *
   * @param id идентификатор удаляемой задачи
   */
  public void deleteById(int id) {
    taskRepository.deleteById(id);
  }

  /**
   * Проверяет существование задачи по идентификатору.
   *
   * @param id идентификатор задачи
   * @return true если задача существует, иначе false
   */
  public boolean existsById(int id) {
    return taskRepository.existById(id);
  }

  /**
   * Инициализирует кэш задач при старте приложения. Загружает все задачи из репозитория в кэш,
   * используя заголовок как ключ. Аннотация {@link PostConstruct} гарантирует выполнение после
   * внедрения зависимостей.
   */
  @PostConstruct
  public void initCache() {
    List<Task> allTasks = taskRepository.findall();
    for (Task task : allTasks) {
      taskCache.put(task.getTitle(), task);
    }
    System.out.println("Cache initialized with " + taskCache.size() + " tasks");
  }

  /**
   * Выполняет очистку ресурсов перед уничтожением бина. Выводит размер кэша в консоль для
   * демонстрации работы {@link PreDestroy}. Вызывается контейнером Spring перед закрытием контекста
   * приложения.
   */
  @PreDestroy
  public void destroyCache() {
    System.out.println("Cache size before destroy: " + taskCache.size());
  }
}