package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.scope.RequestScopedBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для демонстрации работы request скоупа.
 * Показывает, что для каждого HTTP запроса создается новый экземпляр RequestScopedBean.
 *
 * @see RequestScopedBean
 */
@RestController
@RequestMapping("/api/demo/scope")
public class RequestScopedBeanController {

  private final RequestScopedBean requestScopedBean;

  /**
   * Конструктор с внедрением request скоуп бина.
   *
   * @param requestScopedBean бин с областью видимости request
   */
  public RequestScopedBeanController(RequestScopedBean requestScopedBean) {
    this.requestScopedBean = requestScopedBean;
  }

  /**
   * Возвращает информацию о текущем request бине.
   * HTTP метод: GET /api/demo/scope/request-info
   *
   * @return Map с requestId, временем создания и информацией о контроллере
   */
  @GetMapping("/request-info")
  public Map<String, Object> getRequestInfo() {
    Map<String, Object> response = new HashMap<>();

    response.put("requestId", requestScopedBean.getRequestId());
    response.put("creationTime", requestScopedBean.getCreationTime().toString());
    response.put("currentTime", Instant.now().toString());
    response.put("controllerInstance", "Controller-" + System.identityHashCode(this));

    return response;
  }

  /**
   * Демонстрирует, что в рамках одного запроса используется один и тот же request бин.
   * HTTP метод: GET /api/demo/scope/request-multiple-calls
   *
   * @return Map с результатами двух вызовов в рамках одного запроса
   */
  @GetMapping("/request-multiple-calls")
  public Map<String, Object> demonstrateSameRequest() {
    Map<String, Object> response = new HashMap<>();

    response.put("firstCall_requestId", requestScopedBean.getRequestId());
    response.put("firstCall_creationTime", requestScopedBean.getCreationTime().toString());

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    response.put("secondCall_requestId", requestScopedBean.getRequestId());
    response.put("secondCall_creationTime", requestScopedBean.getCreationTime().toString());

    response.put("areSame",
        requestScopedBean.getRequestId().equals(requestScopedBean.getRequestId()));
    response.put("note", "В рамках одного HTTP-запроса requestId одинаковый");

    return response;
  }

  /**
   * Возвращает подробную информацию о request бине.
   * HTTP метод: GET /api/demo/scope/request-details
   *
   * @return детальная информация о бине, включая hashCode
   */
  @GetMapping("/request-details")
  public Map<String, Object> getRequestDetails() {
    return Map.of(
        "requestId", requestScopedBean.getRequestId(),
        "creationTime", requestScopedBean.getCreationTime().toString(),
        "hashCode", System.identityHashCode(requestScopedBean),
        "beanClass", requestScopedBean.getClass().getSimpleName(),
        "note", "При каждом новом запросе hashCode будет разным!"
    );
  }
}