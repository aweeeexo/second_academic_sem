package com.mipt.todolistmanager.config;

import com.mipt.todolistmanager.repository.StubTaskRepository;
import com.mipt.todolistmanager.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для создания бинов репозиториев. Демонстрирует создание бина через @Bean с
 * указанием имени.
 *
 * @see StubTaskRepository
 * @see TaskRepository
 */
@Configuration
public class TaskConfiguration {

  /**
   * Создает бин репозитория-заглушки с именем "StubTask". Используется для демонстрации @Qualifier
   * при внедрении зависимости.
   *
   * @return экземпляр StubTaskRepository
   */
  @Bean("StubTask")
  public TaskRepository taskRepository() {
    System.out.println("StubTask bean created");
    return new StubTaskRepository();
  }
}