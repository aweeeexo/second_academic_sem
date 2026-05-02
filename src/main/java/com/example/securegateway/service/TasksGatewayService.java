package com.example.securegateway.service;

import com.example.securegateway.client.ExternalTasksClient;
import com.example.securegateway.dto.TaskDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class TasksGatewayService {

  private static final Logger log = LoggerFactory.getLogger(TasksGatewayService.class);
  private final ExternalTasksClient externalTasksClient;

  public TasksGatewayService(ExternalTasksClient externalTasksClient) {
    this.externalTasksClient = externalTasksClient;
  }

  @CircuitBreaker(name = "externalApi", fallbackMethod = "createTaskFallback")
  @RateLimiter(name = "externalApi")
  public TaskDto createTask(TaskDto task) {
    return externalTasksClient.createTask(task);
  }

  @CircuitBreaker(name = "externalApi", fallbackMethod = "getTaskFallback")
  @RateLimiter(name = "externalApi")
  public TaskDto getTask(Long id) {
    return externalTasksClient.getTask(id);
  }

  @CircuitBreaker(name = "externalApi", fallbackMethod = "getTasksFallback")
  @RateLimiter(name = "externalApi")
  public List<TaskDto> getTasks(Boolean completed, Integer limit) {
    return externalTasksClient.getTasks(completed, limit);
  }

  @CircuitBreaker(name = "externalApi", fallbackMethod = "deleteTaskFallback")
  @RateLimiter(name = "externalApi")
  public void deleteTask(Long id) {
    externalTasksClient.deleteTask(id);
  }

  public TaskDto createTaskFallback(TaskDto task, Throwable t) {
    log.warn("Fallback: create task failed", t);
    return new TaskDto(null, "Fallback task (circuit open)", false);
  }

  public TaskDto getTaskFallback(Long id, Throwable t) {
    log.warn("Fallback: get task {} failed", id, t);
    return new TaskDto(id, "Fallback task (service unavailable)", null);
  }

  public List<TaskDto> getTasksFallback(Boolean completed, Integer limit, Throwable t) {
    log.warn("Fallback: get tasks failed", t);
    return Collections.emptyList();
  }

  public void deleteTaskFallback(Long id, Throwable t) {
    log.warn("Fallback: delete task {} failed", id, t);
  }
}