package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.scope.PrototypeScopedBean;
import com.mipt.todolistmanager.scope.RequestScopedBean;
import com.mipt.todolistmanager.service.PrototypeDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для демонстрации различных скоупов (областей видимости) бинов в Spring. Позволяет
 * сравнить поведение singleton, request и prototype бинов.
 *
 * @see RequestScopedBean
 * @see PrototypeScopedBean
 * @see PrototypeDemoService
 */
@RestController
@RequestMapping("/api/demo/scope")
public class FullScopeDemoController {

  private final RequestScopedBean requestScopedBean;
  private final PrototypeDemoService prototypeDemoService;
  private final String controllerId;

  /**
   * Конструктор с внедрением зависимостей.
   *
   * @param requestScopedBean    бин с request скоупом
   * @param prototypeDemoService сервис для работы с prototype бинами
   */
  public FullScopeDemoController(RequestScopedBean requestScopedBean,
      PrototypeDemoService prototypeDemoService) {
    this.requestScopedBean = requestScopedBean;
    this.prototypeDemoService = prototypeDemoService;
    this.controllerId = "Controller-" + System.identityHashCode(this);
  }

  /**
   * Сравнивает все три типа скоупов в одном ответе. HTTP метод: GET /api/demo/scope/compare
   *
   * @return Map с информацией о singleton, request и prototype бинах
   */
  @GetMapping("/compare")
  public Map<String, Object> compareAllScopes() {
    Map<String, Object> result = new HashMap<>();

    result.put("singleton_controller", Map.of(
        "instanceId", controllerId,
        "hashCode", System.identityHashCode(this),
        "description", "Всегда один и тот же экземпляр"
    ));

    result.put("request_scope", requestScopedBean.getRequestInfo());

    PrototypeScopedBean prototype1 = prototypeDemoService.getPrototypeBean();
    PrototypeScopedBean prototype2 = prototypeDemoService.getPrototypeBean();

    result.put("prototype_scope", Map.of(
        "bean1", prototype1.getBeanInfo(),
        "bean2", prototype2.getBeanInfo(),
        "are_same_bean", prototype1 == prototype2,
        "description", "Каждый раз новые экземпляры (разные hashCodes)"
    ));

    return result;
  }

  /**
   * Демонстрирует работу request скоупа. HTTP метод: GET /api/demo/scope/test-request
   *
   * @return Map с информацией о request бине для текущего запроса
   */
  @GetMapping("/test-request")
  public Map<String, Object> testRequestScope() {
    return Map.of(
        "requestId", requestScopedBean.getRequestId(),
        "creationTime", requestScopedBean.getCreationTime().toString(),
        "controllerInstance", controllerId,
        "note", "Request ID меняется при каждом запросе, controllerInstance всегда тот же"
    );
  }

  /**
   * Демонстрирует работу prototype скоупа. HTTP метод: GET /api/demo/scope/test-prototype
   *
   * @return Map с информацией о prototype бине и сгенерированных ID
   */
  @GetMapping("/test-prototype")
  public Map<String, Object> testPrototypeScope() {
    PrototypeScopedBean bean = prototypeDemoService.getPrototypeBean();

    return Map.of(
        "beanInstanceId", bean.getBeanInstanceId(),
        "instanceInfo", bean.getInstanceInfo(),
        "generatedId1", bean.generateNextTaskId(),
        "generatedId2", bean.generateNextTaskId(),
        "note", "Каждый вызов создает новый экземпляр, поэтому счетчик всегда начинается с 1"
    );
  }
}