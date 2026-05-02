package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.service.PreferencesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PreferencesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PreferencesService preferencesService;

  @Test
  void getViewPreference_WithCookie_ShouldReturnPreference() throws Exception {
    when(preferencesService.getViewPreference(any())).thenReturn("compact");

    mockMvc.perform(get("/api/preferences/view")
            .cookie(new jakarta.servlet.http.Cookie("viewPreference", "compact")))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(content().string("compact"));
  }

  @Test
  void getViewPreference_WithoutCookie_ShouldReturnDefault() throws Exception {
    when(preferencesService.getViewPreference(any())).thenReturn("detailed");

    mockMvc.perform(get("/api/preferences/view"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(content().string("detailed"));
  }

  @Test
  void setViewPreference_ShouldSetCookie() throws Exception {
    doNothing().when(preferencesService).setViewPreference(eq("compact"), any());

    mockMvc.perform(post("/api/preferences/view")
            .param("mode", "compact"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"));
  }
}