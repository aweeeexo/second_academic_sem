package com.mipt.todolistmanager.repository;

import com.mipt.todolistmanager.model.Priority;
import com.mipt.todolistmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {
  List<Task> findByCompletedAndPriority(boolean completed, Priority priority);

  @Query(value = "SELECT * FROM tasks WHERE due_date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '7' DAY", nativeQuery = true)
  List<Task> findDueInNext7Days();

  @Query("SELECT t FROM Task t LEFT JOIN FETCH t.attachments")
  List<Task> findAllWithAttachments();
}