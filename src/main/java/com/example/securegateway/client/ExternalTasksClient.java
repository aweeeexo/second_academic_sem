package com.example.securegateway.client;

import com.example.securegateway.dto.TaskDto;
import com.example.securegateway.exception.ExternalApiException;
import com.example.securegateway.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

@Component
public class ExternalTasksClient {

  private static final Logger log = LoggerFactory.getLogger(ExternalTasksClient.class);
  private final RestClient restClient;

  public ExternalTasksClient(@Qualifier("externalRestClient") RestClient restClient) {
    this.restClient = restClient;
  }

  public TaskDto createTask(TaskDto task) {
    return restClient.post()
        .uri("/tasks")
        .body(task)
        .retrieve()
        .onStatus(HttpStatusCode::isError, (req, res) -> {
          log.error("Error creating task: {}", res.getStatusCode());
          throw new ExternalApiException("External API error: " + res.getStatusCode());
        })
        .toEntity(TaskDto.class)
        .getBody();
  }

  public TaskDto getTask(Long id) {
    return restClient.get()
        .uri("/tasks/{id}", id)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
          if (res.getStatusCode().value() == 404) {
            try {
              ProblemDetail pd = res.bodyTo(ProblemDetail.class);
              String detail = (pd != null && pd.getDetail() != null) ? pd.getDetail() : "Task not found";
              throw new TaskNotFoundException(detail);
            } catch (Exception e) {
              throw new TaskNotFoundException("Task not found");
            }
          }
          throw new ExternalApiException("Client error: " + res.getStatusCode());
        })
        .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
          log.error("External server error: {}", res.getStatusCode());
          throw new ExternalApiException("External API server error");
        })
        .body(TaskDto.class);
  }

  public List<TaskDto> getTasks(Boolean completed, Integer limit) {
    return restClient.get()
        .uri(uriBuilder -> {
          var builder = uriBuilder.path("/tasks");
          if (completed != null) builder.queryParam("completed", completed);
          if (limit != null) builder.queryParam("limit", limit);
          return builder.build();
        })
        .retrieve()
        .onStatus(HttpStatusCode::isError, (req, res) -> {
          throw new ExternalApiException("Failed to fetch tasks");
        })
        .body(new ParameterizedTypeReference<>() {});
  }

  public void deleteTask(Long id) {
    restClient.delete()
        .uri("/tasks/{id}", id)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
          if (res.getStatusCode().value() == 404) {
            throw new TaskNotFoundException("Task not found for deletion");
          }
          throw new ExternalApiException("Client error on delete");
        })
        .toBodilessEntity();
  }
}