package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.dto.TaskCreateDto;
import com.mipt.todolistmanager.dto.TaskResponseDto;
import com.mipt.todolistmanager.dto.TaskUpdateDto;
import com.mipt.todolistmanager.exception.TaskNotFoundException;
import com.mipt.todolistmanager.mapper.TaskMapper;
import com.mipt.todolistmanager.model.Priority;
import com.mipt.todolistmanager.model.Task;
import com.mipt.todolistmanager.repository.InMemoryTaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления задачами. Содержит бизнес-логику приложения и делегирует операции
 * репозиторию. Демонстрирует жизненный цикл бина через аннотации {@link PostConstruct} и
 * {@link PreDestroy}.
 */
@Service
public class TaskService {

  private final InMemoryTaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final Map<String, Task> taskCache = new HashMap<>();

  public TaskService(InMemoryTaskRepository taskRepository, TaskMapper taskMapper) {
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
  }

  public List<TaskResponseDto> findAll() {
    if (taskRepository.findAll().isEmpty()) {
      System.out.println("Tasks are not exists yet");
    }
    return taskRepository.findAll().stream()
        .map(taskMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  public Optional<Task> findById(int id) {
    return taskRepository.findById(id);
  }

  public TaskResponseDto getTaskById(int id) {
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id));
    return taskMapper.toResponseDto(task);
  }

  public Task save(Task task) {
    if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("Task title cannot be empty");
    }
    return taskRepository.save(task);
  }

  public TaskResponseDto createTask(TaskCreateDto dto) {
    Task task = taskMapper.toEntity(dto);
    task.setCreatedAt(LocalDateTime.now());
    task.setCompleted(false);
    Task saved = taskRepository.save(task);
    return taskMapper.toResponseDto(saved);
  }

  public TaskResponseDto updateTask(int id, TaskUpdateDto dto) {
    Task existing = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id));
    taskMapper.updateEntity(dto, existing);
    if (dto.getDueDate() != null && dto.getDueDate().isBefore(existing.getCreatedAt().toLocalDate())) {
      throw new IllegalArgumentException("Due date cannot be before creation date");
    }
    Task saved = taskRepository.save(existing);
    return taskMapper.toResponseDto(saved);
  }

  public void deleteById(int id) {
    if (!taskRepository.existsById(id)) {
      throw new TaskNotFoundException(id);
    }
    taskRepository.deleteById(id);
  }

  public boolean existsById(int id) {
    return taskRepository.existsById(id);
  }

  public int getTotalCount() {
    return taskRepository.findAll().size();
  }

  @PostConstruct
  public void initCache() {
    List<Task> allTasks = taskRepository.findAll();
    for (Task task : allTasks) {
      taskCache.put(task.getTitle(), task);
    }
    System.out.println("Cache initialized with " + taskCache.size() + " tasks");
  }

  @PreDestroy
  public void destroyCache() {
    System.out.println("Cache size before destroy: " + taskCache.size());
  }
}