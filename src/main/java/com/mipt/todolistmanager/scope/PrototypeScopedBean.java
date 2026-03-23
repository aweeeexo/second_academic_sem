package com.mipt.todolistmanager.scope;

import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Бин с областью видимости "prototype".
 * Создается новый экземпляр при каждом обращении к контейнеру Spring.
 * Демонстрирует жизненный цикл prototype скоупа и генерацию уникальных ID.
 *
 * @see Scope
 */
@Component
@Scope("prototype")
public class PrototypeScopedBean {

  private final String beanInstanceId;
  private final AtomicLong taskIdGenerator;
  private final String instanceInfo;

  /**
   * Конструктор, инициализирующий уникальные идентификаторы экземпляра.
   * Вызывается при каждом создании нового бина.
   */
  public PrototypeScopedBean() {
    this.beanInstanceId = UUID.randomUUID().toString();
    this.taskIdGenerator = new AtomicLong(1);
    this.instanceInfo = "PrototypeBean-" + System.identityHashCode(this);

    System.out.println("Instance: " + instanceInfo);
    System.out.println("Bean Instance ID: " + beanInstanceId);
    System.out.println("Initial next ID: " + taskIdGenerator.get());
  }

  /**
   * Генерирует следующий уникальный ID для задачи.
   * Каждый экземпляр бина имеет свой собственный счетчик.
   *
   * @return следующий ID задачи
   */
  public Long generateNextTaskId() {
    return taskIdGenerator.getAndIncrement();
  }

  /**
   * Генерирует UUID для задачи (альтернативный метод).
   *
   * @return UUID
   */
  public String generateUuidTaskId() {
    return UUID.randomUUID().toString();
  }

  /**
   * Возвращает уникальный идентификатор экземпляра бина.
   *
   * @return идентификатор экземпляра
   */
  public String getBeanInstanceId() {
    return beanInstanceId;
  }

  /**
   * Возвращает информацию об экземпляре.
   *
   * @return информация об экземпляре
   */
  public String getInstanceInfo() {
    return instanceInfo;
  }

  /**
   * Возвращает текущее значение счетчика.
   *
   * @return текущее значение счетчика
   */
  public long getCurrentCounterValue() {
    return taskIdGenerator.get();
  }

  /**
   * Возвращает полную информацию о бине.
   *
   * @return Map с информацией о бине
   */
  public Map<String, Object> getBeanInfo() {
    return Map.of(
        "beanInstanceId", beanInstanceId,
        "instanceInfo", instanceInfo,
        "currentCounterValue", taskIdGenerator.get(),
        "hashCode", System.identityHashCode(this)
    );
  }
}