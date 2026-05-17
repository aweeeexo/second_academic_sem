package com.mipt.todolistmanager.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class PreferencesService {
  private static final String VIEW_PREFERENCE_COOKIE = "viewPreference";

  public String getViewPreference(Cookie[] cookies) {
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (VIEW_PREFERENCE_COOKIE.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return "detailed";
  }

  public void setViewPreference(String mode, HttpServletResponse response) {
    Cookie cookie = new Cookie(VIEW_PREFERENCE_COOKIE, mode);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24 * 30);
    response.addCookie(cookie);
  }
}