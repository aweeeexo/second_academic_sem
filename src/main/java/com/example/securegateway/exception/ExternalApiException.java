package com.example.securegateway.exception;

public class ExternalApiException extends RuntimeException {
  public ExternalApiException(String message) {
    super(message);
  }
}