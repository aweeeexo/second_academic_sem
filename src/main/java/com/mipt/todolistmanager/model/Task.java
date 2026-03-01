package com.mipt.todolistmanager.model;

import java.util.Objects;

/**
 * Модель данных для задачи. Содержит основные поля задачи и переопределенные методы equals,
 * hashCode и toString.
 */
public class Task {

  private int id;
  private String title;
  private String description;
  private boolean completed;

  /**
   * Конструктор по умолчанию.
   */
  public Task() {
  }

  /**
   * Конструктор со всеми полями.
   *
   * @param id          идентификатор задачи
   * @param title       заголовок задачи
   * @param description описание задачи
   * @param completed   статус выполнения
   */
  public Task(int id, String title, String description, boolean completed) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.completed = completed;
  }

  /**
   * Возвращает идентификатор задачи.
   *
   * @return идентификатор
   */
  public int getId() {
    return id;
  }

  /**
   * Устанавливает идентификатор задачи.
   *
   * @param id идентификатор
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Возвращает заголовок задачи.
   *
   * @return заголовок
   */
  public String getTitle() {
    return title;
  }

  /**
   * Устанавливает заголовок задачи.
   *
   * @param title заголовок
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Возвращает описание задачи.
   *
   * @return описание
   */
  public String getDescription() {
    return description;
  }

  /**
   * Устанавливает описание задачи.
   *
   * @param description описание
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Возвращает статус выполнения задачи.
   *
   * @return true если задача выполнена
   */
  public boolean getCompleted() {
    return completed;
  }

  /**
   * Устанавливает статус выполнения задачи.
   *
   * @param completed статус выполнения
   */
  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  /**
   * Возвращает строковое представление задачи.
   *
   * @return строковое представление
   */
  @Override
  public String toString() {
    return "Task: " + title +
        " id: " + id +
        " description: " + description +
        " completed: " + completed;
  }

  /**
   * Сравнивает задачу с другим объектом.
   *
   * @param o объект для сравнения
   * @return true если объекты равны
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Task task = (Task) o;
    return completed == task.completed &&
        id == task.getId() &&
        Objects.equals(title, task.getTitle()) &&
        Objects.equals(description, task.getDescription());
  }

  /**
   * Возвращает хэш-код задачи.
   *
   * @return хэш-код
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, completed);
  }
}