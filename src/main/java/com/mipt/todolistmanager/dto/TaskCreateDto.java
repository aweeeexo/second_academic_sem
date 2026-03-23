package com.mipt.todolistmanager.dto;

import com.mipt.todolistmanager.model.Priority;
import com.mipt.todolistmanager.validation.OnCreate;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

public class TaskCreateDto {
  @NotBlank(groups = OnCreate.class)
  @Size(min = 3, max = 100, groups = OnCreate.class)
  private String title;

  @Size(max = 500, groups = OnCreate.class)
  private String description;

  @FutureOrPresent(groups = OnCreate.class)
  private LocalDate dueDate;

  @NotNull(groups = OnCreate.class)
  private Priority priority;

  @Size(max = 5, groups = OnCreate.class)
  private Set<String> tags;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
}