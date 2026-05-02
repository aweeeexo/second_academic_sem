package com.mipt.todolistmanager.repository;

import com.mipt.todolistmanager.model.Priority;
import com.mipt.todolistmanager.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

  @Autowired
  private TaskJpaRepository taskRepository;

  @Test
  void testFindByCompletedAndPriority() {
    Task task = new Task();
    task.setTitle("Test");
    task.setCompleted(true);
    task.setPriority(Priority.HIGH);
    task.setTags(Set.of("tag1"));
    taskRepository.save(task);

    List<Task> found = taskRepository.findByCompletedAndPriority(true, Priority.HIGH);
    assertThat(found).hasSize(1);
    assertThat(found.get(0).getTitle()).isEqualTo("Test");
  }

  @Test
  void testFindDueInNext7Days() {
    Task task = new Task();
    task.setTitle("Due soon");
    task.setDueDate(LocalDate.now().plusDays(3));
    taskRepository.save(task);

    List<Task> due = taskRepository.findDueInNext7Days();
    assertThat(due).contains(task);
  }
}