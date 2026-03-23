package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.dto.TaskResponseDto;
import com.mipt.todolistmanager.service.FavoritesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {
  private final FavoritesService favoritesService;

  @Value("${api.version}")
  private String apiVersion;

  public FavoritesController(FavoritesService favoritesService) {
    this.favoritesService = favoritesService;
  }

  @Operation(summary = "Add task to favorites")
  @PostMapping("/{taskId}")
  public ResponseEntity<Void> addToFavorites(@PathVariable int taskId, HttpSession session) {
    favoritesService.addToFavorites(taskId, session);
    return ResponseEntity.ok()
        .header("X-API-Version", apiVersion)
        .build();
  }

  @Operation(summary = "Remove task from favorites")
  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> removeFromFavorites(@PathVariable int taskId, HttpSession session) {
    favoritesService.removeFromFavorites(taskId, session);
    return ResponseEntity.noContent()
        .header("X-API-Version", apiVersion)
        .build();
  }

  @Operation(summary = "Get favorite tasks")
  @GetMapping
  public ResponseEntity<List<TaskResponseDto>> getFavorites(HttpSession session) {
    List<TaskResponseDto> favorites = favoritesService.getFavorites(session);
    return ResponseEntity.ok()
        .header("X-API-Version", apiVersion)
        .body(favorites);
  }
}