package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.scope.PrototypeScopedBean;
import com.mipt.todolistmanager.scope.RequestScopedBean;
import com.mipt.todolistmanager.service.PrototypeDemoService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для детальной демонстрации работы prototype скоупа.
 * Показывает создание новых экземпляров prototype бинов и генерацию ID задач.
 *
 * @see PrototypeScopedBean
 * @see PrototypeDemoService
 */
@RestController
@RequestMapping("/api/demo/scope")
public class PrototypeScopedDemoController {

  private final PrototypeDemoService prototypeDemoService;
  private final RequestScopedBean requestScopedBean;

  /**
   * Конструктор с внедрением зависимостей.
   *
   * @param requestScopedBean бин с request скоупом
   * @param prototypeDemoService сервис для работы с prototype бинами
   */
  public PrototypeScopedDemoController(RequestScopedBean requestScopedBean,
      PrototypeDemoService prototypeDemoService) {
    this.requestScopedBean = requestScopedBean;
    this.prototypeDemoService = prototypeDemoService;
  }

  /**
   * Демонстрирует создание новых экземпляров prototype бинов.
   * HTTP метод: GET /api/demo/scope/prototype
   *
   * @return информация о созданных prototype бинах
   */
  @GetMapping("/prototype")
  public Map<String, Object> demonstratePrototypeScope() {
    return prototypeDemoService.demonstratePrototypeScope();
  }

  /**
   * Демонстрирует генерацию ID задач с использованием prototype бина.
   * HTTP метод: GET /api/demo/scope/prototype/generate-ids
   *
   * @param count количество ID для генерации (по умолчанию 3)
   * @return информация о бине и сгенерированных ID
   */
  @GetMapping("/prototype/generate-ids")
  public Map<String, Object> generateTaskIds(@RequestParam(defaultValue = "3") int count) {
    Map<String, Object> result = new HashMap<>();
    PrototypeScopedBean bean = prototypeDemoService.getPrototypeBean();

    List<Long> generatedIds = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      generatedIds.add(bean.generateNextTaskId());
    }

    result.put("bean_info", bean.getBeanInfo());
    result.put("generated_ids", generatedIds);
    result.put("note", "Каждый вызов этого эндпоинта создает новый экземпляр бина, поэтому счетчик начинается с 1");

    return result;
  }
}