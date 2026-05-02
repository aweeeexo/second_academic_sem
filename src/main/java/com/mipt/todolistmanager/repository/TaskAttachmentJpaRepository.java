package com.mipt.todolistmanager.repository;

import com.mipt.todolistmanager.model.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskAttachmentJpaRepository extends JpaRepository<TaskAttachment, Long> {
  List<TaskAttachment> findByTaskId(Long taskId);
}