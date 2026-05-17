package com.mipt.todolistmanager.repository;

import com.mipt.todolistmanager.model.TaskAttachment;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class TaskAttachmentRepository {
  private final Map<Long, TaskAttachment> storage = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  public TaskAttachment save(TaskAttachment attachment) {
    if (attachment.getId() == null) {
      attachment.setId(idGenerator.getAndIncrement());
    }
    storage.put(attachment.getId(), attachment);
    return attachment;
  }

  public TaskAttachment findById(Long id) {
    return storage.get(id);
  }

  public Map<Long, TaskAttachment> findByTaskId(Long taskId) {
    return storage.values().stream()
        .filter(a -> a.getTaskId().equals(taskId))
        .collect(Collectors.toMap(TaskAttachment::getId, a -> a));
  }

  public void deleteById(Long id) {
    storage.remove(id);
  }
}