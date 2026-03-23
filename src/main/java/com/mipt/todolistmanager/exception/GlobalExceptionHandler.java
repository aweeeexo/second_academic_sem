package com.mipt.todolistmanager.exception;

import com.mipt.todolistmanager.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
    Map<String, Object> details = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      details.put(fieldName, errorMessage);
    });
    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", ex.getMessage(), request, details);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
    Map<String, Object> details = new HashMap<>();
    ex.getConstraintViolations().forEach(violation -> {
      details.put(violation.getPropertyPath().toString(), violation.getMessage());
    });
    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, "Constraint violation", ex.getMessage(), request, details);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, "Missing parameter", ex.getMessage(), request, null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON", ex.getMessage(), request, null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND, "Not found", ex.getMessage(), request, null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND, "Task not found", ex.getMessage(), request, null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad request", ex.getMessage(), request, null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "An unexpected error occurred", request, null);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  private ErrorResponse buildErrorResponse(HttpStatus status, String error, String message, HttpServletRequest request, Map<String, Object> details) {
    ErrorResponse response = new ErrorResponse();
    response.setTimestamp(Instant.now());
    response.setStatus(status.value());
    response.setError(error);
    response.setMessage(message);
    response.setPath(request.getRequestURI());
    response.setDetails(details);
    return response;
  }
}