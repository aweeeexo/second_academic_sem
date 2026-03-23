package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.model.Task;
import com.mipt.todolistmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskStatisticsService {

  private final TaskRepository taskRepository;

  public TaskStatisticsService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public long getTotalTasksCount() {
    return taskRepository.findAll().size();
  }

  public long getCompletedTasksCount() {
    return taskRepository.findAll().stream()
        .filter(Task::isCompleted)
        .count();
  }
}