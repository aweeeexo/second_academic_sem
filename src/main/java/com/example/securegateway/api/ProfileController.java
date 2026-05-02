package com.example.securegateway.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {

  @GetMapping("/profile")
  @PreAuthorize("hasRole('USER')")
  public Map<String, String> profile(@AuthenticationPrincipal UserDetails user) {
    return Map.of("username", user.getUsername(), "roles", user.getAuthorities().toString());
  }

  @GetMapping("/docs")
  @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
  public String docs() {
    return "Documentation content (only for READ_PRIVILEGE)";
  }
}