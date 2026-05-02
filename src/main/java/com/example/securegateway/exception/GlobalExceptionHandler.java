package com.example.securegateway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleNotFound(TaskNotFoundException ex) {
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    pd.setType(URI.create("https://api.example.com/errors/not-found"));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
  }

  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity<ProblemDetail> handleExternalApi(ExternalApiException ex) {
    log.error("External API error: {}", ex.getMessage());
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, "External service error");
    pd.setProperty("reason", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(pd);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleGeneric(Exception ex) {
    log.error("Unexpected error", ex);
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
  }
}