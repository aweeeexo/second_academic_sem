package com.mipt.todolistmanager.aspect;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования вызовов методов в сервисном слое. Использует Around advice для логирования
 * входа, выхода и исключений.
 *
 * @see Aspect
 */
@Aspect
@Component
public class LoggingAspect {

  /**
   * Логирует все методы в пакете service. Перехватывает вызов, логирует аргументы, время выполнения
   * и результат.
   *
   * @param joinPoint точка соединения с информацией о вызываемом методе
   * @return результат выполнения метода
   * @throws Throwable если метод выбросил исключение
   */
  @Around("execution(* com.mipt.todolistmanager.service.*.*(..))")
  public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().toShortString();
    Object[] args = joinPoint.getArgs();

    System.out.println("Entering " + methodName + " with args: " + Arrays.toString(args));

    Object result;
    try {
      result = joinPoint.proceed();
    } catch (Throwable t) {
      System.out.println("Exception in " + methodName + ": " + t);
      throw t;
    }

    System.out.println("Exiting " + methodName + " with result: " + result);
    return result;
  }
}