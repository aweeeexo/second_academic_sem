package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.service.PreferencesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {
  private final PreferencesService preferencesService;
  @Value("${api.version}")
  private String apiVersion;

  public PreferencesController(PreferencesService preferencesService) {
    this.preferencesService = preferencesService;
  }

  @Operation(summary = "Get view preference from cookie")
  @GetMapping("/view")
  public ResponseEntity<String> getViewPreference(@CookieValue(value = "viewPreference", required = false) Cookie cookie) {
    String preference = preferencesService.getViewPreference(cookie != null ? new Cookie[]{cookie} : null);
    return ResponseEntity.ok()
        .header("X-API-Version", apiVersion)
        .body(preference);
  }

  @Operation(summary = "Set view preference")
  @PostMapping("/view")
  public ResponseEntity<Void> setViewPreference(@RequestParam String mode, HttpServletResponse response) {
    preferencesService.setViewPreference(mode, response);
    return ResponseEntity.ok()
        .header("X-API-Version", apiVersion)
        .build();
  }
}