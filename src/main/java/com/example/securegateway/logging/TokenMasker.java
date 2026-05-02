package com.example.securegateway.logging;

public class TokenMasker {
  public static String mask(String token) {
    if (token == null || token.length() < 12) return "***";
    return token.substring(0, 6) + "..." + token.substring(token.length() - 6);
  }
}