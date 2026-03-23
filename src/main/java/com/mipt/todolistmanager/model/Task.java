package com.mipt.todolistmanager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Модель данных для задачи. Содержит основные поля задачи и переопределенные методы equals,
 * hashCode и toString.
 */
public class Task {

  private Long id; // изменено на Long для удобства
  private String title;
  private String description;
  private boolean completed;
  private LocalDateTime createdAt;
  private LocalDate dueDate;
  private Priority priority;
  private Set<String> tags;

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
  public Task(Long id, String title, String description, boolean completed,
      LocalDateTime createdAt, LocalDate dueDate, Priority priority, Set<String> tags) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.completed = completed;
    this.createdAt = createdAt;
    this.dueDate = dueDate;
    this.priority = priority;
    this.tags = tags;
  }

  /**
   * Возвращает идентификатор задачи.
   *
   * @return идентификатор
   */
  public Long getId() {
    return id;
  }

  /**
   * Устанавливает идентификатор задачи.
   *
   * @param id идентификатор
   */
  public void setId(Long id) {
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
  public boolean isCompleted() {
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
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
        Objects.equals(id, task.id) &&
        Objects.equals(title, task.title) &&
        Objects.equals(description, task.description) &&
        Objects.equals(createdAt, task.createdAt) &&
        Objects.equals(dueDate, task.dueDate) &&
        priority == task.priority &&
        Objects.equals(tags, task.tags);
  }

  /**
   * Возвращает хэш-код задачи.
   *
   * @return хэш-код
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, completed, createdAt, dueDate, priority, tags);
  }
}