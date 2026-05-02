package com.mipt.todolistmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ToDoListManagerApplication {
  public static void main(String[] args) {
    SpringApplication.run(ToDoListManagerApplication.class, args);
  }
}