package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.scope.PrototypeScopedBean;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для демонстрации работы prototype скоупа. Использует Lookup метод для получения новых
 * экземпляров prototype бинов. Демонстрирует, что при каждом обращении к getPrototypeBean()
 * создается новый экземпляр PrototypeScopedBean с собственным состоянием (счетчиком ID).
 *
 * @see PrototypeScopedBean
 * @see Lookup
 */
@Service
public class PrototypeDemoService {

  /**
   * Lookup метод для получения нового экземпляра prototype бина. Spring динамически переопределяет
   * этот метод, возвращая новый экземпляр PrototypeScopedBean при каждом вызове.
   *
   * <p>Использование {@link Lookup} необходимо, так как внедрение prototype-бина
   * напрямую в singleton-бин приведет к созданию только одного экземпляра.</p>
   *
   * @return новый экземпляр PrototypeScopedBean
   */
  @Lookup
  public PrototypeScopedBean getPrototypeBean() {
    // Реализация не требуется - Spring переопределяет метод
    return null;
  }

  /**
   * Демонстрирует создание новых экземпляров prototype бинов. Показывает, что при каждом вызове
   * getPrototypeBean() создается новый экземпляр, и каждый экземпляр имеет свой собственный счетчик
   * для генерации ID.
   *
   * @return Map с информацией о двух созданных бинах и сгенерированных ID, включая сравнение
   * экземпляров (are_beans_same)
   */
  public Map<String, Object> demonstratePrototypeScope() {
    Map<String, Object> result = new HashMap<>();

    PrototypeScopedBean bean1 = getPrototypeBean();
    PrototypeScopedBean bean2 = getPrototypeBean();

    Long id1 = bean1.generateNextTaskId();
    Long id2 = bean1.generateNextTaskId();
    Long id3 = bean2.generateNextTaskId();

    result.put("bean1_info", bean1.getBeanInfo());
    result.put("bean2_info", bean2.getBeanInfo());
    result.put("id_from_bean1_first", id1);
    result.put("id_from_bean1_second", id2);
    result.put("id_from_bean2_first", id3);
    result.put("are_beans_same", bean1 == bean2);

    return result;
  }
}