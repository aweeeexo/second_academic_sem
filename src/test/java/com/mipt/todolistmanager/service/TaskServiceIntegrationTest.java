package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.exception.TaskNotFoundException;
import com.mipt.todolistmanager.model.Task;
import com.mipt.todolistmanager.repository.TaskJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceIntegrationTest {

  @Autowired
  private TaskService taskService;
  @Autowired
  private TaskJpaRepository taskRepository;

  @Test
  void testBulkCompleteTasksRollback() {
    Task task1 = new Task();
    task1.setTitle("Task 1");
    task1.setCompleted(false);
    taskRepository.save(task1);
    List<Long> ids = List.of(task1.getId(), 999L);
    assertThrows(TaskNotFoundException.class, () -> taskService.bulkCompleteTasks(ids));
    Task refreshed = taskRepository.findById(task1.getId()).orElseThrow();
    assertThat(refreshed.isCompleted()).isFalse();
  }

  @Test
  void testBulkCompleteTasksSuccess() {
    Task task1 = new Task();
    task1.setTitle("Task 1");
    task1.setCompleted(false);
    Task task2 = new Task();
    task2.setTitle("Task 2");
    task2.setCompleted(false);
    taskRepository.saveAll(List.of(task1, task2));
    List<Long> ids = List.of(task1.getId(), task2.getId());
    taskService.bulkCompleteTasks(ids);
    assertThat(taskRepository.findById(task1.getId()).get().isCompleted()).isTrue();
    assertThat(taskRepository.findById(task2.getId()).get().isCompleted()).isTrue();
  }
}