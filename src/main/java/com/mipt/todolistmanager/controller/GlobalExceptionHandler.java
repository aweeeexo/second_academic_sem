package com.mipt.todolistmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Глобальный обработчик исключений для REST контроллеров. Преобразует исключения в соответствующие
 * HTTP статусы.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Обрабатывает IllegalArgumentException. Возвращает 400 BAD_REQUEST с сообщением об ошибке.
   *
   * @param e исключение
   * @return ResponseEntity с статусом 400
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(e.getMessage());
  }

  /**
   * Обрабатывает все остальные исключения. Возвращает 500 INTERNAL_SERVER_ERROR.
   *
   * @param e исключение
   * @return ResponseEntity с статусом 500
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Internal server error: " + e.getMessage());
  }
}