package com.mipt.todolistmanager.scope;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Бин с областью видимости "request". Создается новый экземпляр для каждого HTTP-запроса.
 * Демонстрирует жизненный цикл request скоупа.
 *
 * @see RequestScope
 */
@Component
@RequestScope
public class RequestScopedBean {

  private final String requestId = UUID.randomUUID().toString();
  private final Object requestInfo = UUID.randomUUID().toString();
  private final Instant creationTime = Instant.now();

  /**
   * Возвращает уникальный идентификатор текущего запроса.
   *
   * @return requestId
   */
  public String getRequestId() {
    return requestId;
  }

  /**
   * Возвращает время создания бина (начало обработки запроса).
   *
   * @return время создания
   */
  public Instant getCreationTime() {
    return creationTime;
  }

  /**
   * Возвращает дополнительную информацию о запросе.
   *
   * @return информация о запросе
   */
  public Object getRequestInfo() {
    return requestInfo;
  }
}