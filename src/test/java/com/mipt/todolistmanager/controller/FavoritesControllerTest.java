package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.dto.TaskResponseDto;
import com.mipt.todolistmanager.service.FavoritesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritesController.class)
@ActiveProfiles("test")
class FavoritesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FavoritesService favoritesService;

  private TaskResponseDto taskDto;
  private MockHttpSession session;

  @BeforeEach
  void setUp() {
    session = new MockHttpSession();

    taskDto = new TaskResponseDto();
    taskDto.setId(1L);
    taskDto.setTitle("Favorite Task");
    taskDto.setDescription("Description");
    taskDto.setCompleted(false);
  }

  @Test
  void addToFavorites_ShouldReturn200() throws Exception {
    doNothing().when(favoritesService).addToFavorites((int) eq(1L), any());

    mockMvc.perform(post("/api/favorites/1")
            .session(session))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"));
  }

  @Test
  void addToFavorites_WhenTaskNotFound_ShouldReturn404() throws Exception {
    doNothing().when(favoritesService).addToFavorites((int) eq(999L), any());

    mockMvc.perform(post("/api/favorites/999")
            .session(session))
        .andExpect(status().isOk());
  }

  @Test
  void removeFromFavorites_ShouldReturn204() throws Exception {
    doNothing().when(favoritesService).removeFromFavorites((int) eq(1L), any());

    mockMvc.perform(delete("/api/favorites/1")
            .session(session))
        .andExpect(status().isNoContent())
        .andExpect(header().string("X-API-Version", "2.0.0"));
  }

  @Test
  void getFavorites_ShouldReturnList() throws Exception {
    List<TaskResponseDto> favorites = Arrays.asList(taskDto);
    when(favoritesService.getFavorites(any())).thenReturn(favorites);

    mockMvc.perform(get("/api/favorites")
            .session(session))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].title").value("Favorite Task"));
  }

  @Test
  void getFavorites_WhenEmpty_ShouldReturnEmptyList() throws Exception {
    when(favoritesService.getFavorites(any())).thenReturn(List.of());

    mockMvc.perform(get("/api/favorites")
            .session(session))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(jsonPath("$").isEmpty());
  }
}