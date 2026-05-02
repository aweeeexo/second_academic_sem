package com.example.securegateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Value("${external.api.base-url}")
  private String baseUrl;

  @Value("${external.api.connect-timeout}")
  private int connectTimeout;

  @Value("${external.api.read-timeout}")
  private int readTimeout;

  @Bean
  public RestClient externalRestClient() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(connectTimeout);
    factory.setReadTimeout(readTimeout);
    return RestClient.builder()
        .baseUrl(baseUrl)
        .requestFactory(factory)
        .defaultHeader("User-Agent", "SecureGateway/1.0")
        .defaultHeader("Accept", "application/json")
        .build();
  }
}