package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис для сбора статистики о задачах и демонстрации различных способов внедрения зависимостей в
 * Spring.
 *
 * <p>Демонстрирует:
 * <ul>
 *   <li>Использование {@link Qualifier} для выбора конкретного бина репозитория</li>
 *   <li>Внедрение кастомных свойств из application.yml через {@link Value}</li>
 *   <li>Работу с несколькими реализациями одного интерфейса</li>
 * </ul>
 *
 * @see TaskRepository
 * @see com.mipt.todolistmanager.repository.InMemoryTaskRepository
 * @see com.mipt.todolistmanager.repository.StubTaskRepository
 */
@Service
public class TaskStatisticsService {

  private final TaskRepository inMemoryTaskRepository;
  private final TaskRepository stubTaskRepository;

  @Value("${app.name}")
  private String appName;

  @Value("${app.version}")
  private String appVersion;

  /**
   * Конструктор с внедрением двух разных реализаций репозитория.
   *
   * @param inMemoryTaskRepository основной репозиторий (InMemoryTaskRepository)
   * @param stubTaskRepository     репозиторий-заглушка
   */
  public TaskStatisticsService(TaskRepository inMemoryTaskRepository,
      @Qualifier("StubTask") TaskRepository stubTaskRepository) {
    this.inMemoryTaskRepository = inMemoryTaskRepository;
    this.stubTaskRepository = stubTaskRepository;
  }

  // Здесь могут быть методы для сбора статистики, например:

  /**
   * Сравнивает количество задач в разных репозиториях.
   *
   * @return строка со статистикой
   */
  public String compareRepositories() {
    int inMemoryCount = inMemoryTaskRepository.findall().size();
    int stubCount = stubTaskRepository.findall().size();

    return String.format(
        "Application: %s v%s - In-memory repo has %d tasks, Stub repo has %d tasks",
        appName, appVersion, inMemoryCount, stubCount);
  }

  /**
   * Возвращает имя приложения из конфигурации.
   *
   * @return имя приложения
   */
  public String getAppName() {
    return appName;
  }

  /**
   * Возвращает версию приложения из конфигурации.
   *
   * @return версия приложения
   */
  public String getAppVersion() {
    return appVersion;
  }
}