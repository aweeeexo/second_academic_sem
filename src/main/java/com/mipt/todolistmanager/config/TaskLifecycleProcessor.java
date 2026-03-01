package com.mipt.todolistmanager.config;

import com.mipt.todolistmanager.repository.TaskRepository;
import com.mipt.todolistmanager.service.TaskService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Процессор жизненного цикла бинов. Реализует BeanPostProcessor для логирования создания и
 * инициализации бинов типа TaskService и TaskRepository.
 *
 * @see BeanPostProcessor
 */
@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {

  /**
   * Вызывается перед инициализацией бина.
   *
   * @param bean     экземпляр бина
   * @param beanName имя бина
   * @return бин после обработки
   * @throws BeansException если возникла ошибка
   */
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    if (bean instanceof TaskService || bean instanceof TaskRepository) {
      System.out.println("Bean before initialization: " + beanName);
    }
    return bean;
  }

  /**
   * Вызывается после инициализации бина.
   *
   * @param bean     экземпляр бина
   * @param beanName имя бина
   * @return бин после обработки
   * @throws BeansException если возникла ошибка
   */
  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName)
      throws BeansException {
    if (bean instanceof TaskService || bean instanceof TaskRepository) {
      System.out.println("Bean after initialization: " + beanName);
    }
    return bean;
  }
}