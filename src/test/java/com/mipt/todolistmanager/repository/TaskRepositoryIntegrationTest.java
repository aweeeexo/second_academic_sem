package com.mipt.todolistmanager.repository;

import com.mipt.todolistmanager.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
      .withDatabaseName("testdb")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.flyway.enabled", () -> true);
  }

  @Autowired
  private TaskJpaRepository taskRepository;

  @Test
  void findDueInNext7Days_ShouldReturnTasksDueSoon() {
    Task dueIn3Days = new Task();
    dueIn3Days.setTitle("Due in 3 days");
    dueIn3Days.setDueDate(LocalDate.now().plusDays(3));
    taskRepository.save(dueIn3Days);

    Task dueIn10Days = new Task();
    dueIn10Days.setTitle("Due in 10 days");
    dueIn10Days.setDueDate(LocalDate.now().plusDays(10));
    taskRepository.save(dueIn10Days);

    List<Task> dueTasks = taskRepository.findDueInNext7Days();

    assertThat(dueTasks).hasSize(1);
    assertThat(dueTasks.get(0).getTitle()).isEqualTo("Due in 3 days");
  }
}