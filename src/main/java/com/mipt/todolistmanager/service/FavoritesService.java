package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.dto.TaskResponseDto;
import com.mipt.todolistmanager.exception.TaskNotFoundException;
import com.mipt.todolistmanager.mapper.TaskMapper;
import com.mipt.todolistmanager.repository.InMemoryTaskRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritesService {
  private final InMemoryTaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private static final String FAVORITES_SESSION_KEY = "favoriteTaskIds";

  public FavoritesService(InMemoryTaskRepository taskRepository, TaskMapper taskMapper) {
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
  }

  @SuppressWarnings("unchecked")
  public void addToFavorites(Long taskId, HttpSession session) {
    if (!taskRepository.existsById(taskId)) {
      throw new TaskNotFoundException(taskId);
    }
    List<Long> favorites = (List<Long>) session.getAttribute(FAVORITES_SESSION_KEY);
    if (favorites == null) {
      favorites = new ArrayList<>();
      session.setAttribute(FAVORITES_SESSION_KEY, favorites);
    }
    if (!favorites.contains(taskId)) {
      favorites.add(taskId);
    }
  }

  @SuppressWarnings("unchecked")
  public void removeFromFavorites(Long taskId, HttpSession session) {
    List<Long> favorites = (List<Long>) session.getAttribute(FAVORITES_SESSION_KEY);
    if (favorites != null) {
      favorites.remove(taskId);
    }
  }

  @SuppressWarnings("unchecked")
  public List<TaskResponseDto> getFavorites(HttpSession session) {
    List<Long> favorites = (List<Long>) session.getAttribute(FAVORITES_SESSION_KEY);
    if (favorites == null || favorites.isEmpty()) {
      return List.of();
    }
    return favorites.stream()
        .map(taskRepository::findById)
        .filter(task -> task != null)
        .map(taskMapper::toResponseDto)
        .collect(Collectors.toList());
  }
}