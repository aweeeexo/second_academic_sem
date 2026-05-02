package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.dto.AttachmentResponseDto;
import com.mipt.todolistmanager.exception.TaskNotFoundException;
import com.mipt.todolistmanager.model.Task;
import com.mipt.todolistmanager.model.TaskAttachment;
import com.mipt.todolistmanager.repository.TaskAttachmentJpaRepository;
import com.mipt.todolistmanager.repository.TaskJpaRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttachmentService {
  private final TaskAttachmentJpaRepository attachmentRepository;
  private final TaskJpaRepository taskRepository;
  private final Path rootLocation = Paths.get("uploads");

  public AttachmentService(TaskAttachmentJpaRepository attachmentRepository, TaskJpaRepository taskRepository) {
    this.attachmentRepository = attachmentRepository;
    this.taskRepository = taskRepository;
  }

  @Transactional
  public AttachmentResponseDto storeAttachment(Long taskId, MultipartFile file) throws IOException {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException(taskId));
    if (!Files.exists(rootLocation)) {
      Files.createDirectories(rootLocation);
    }
    String originalFilename = file.getOriginalFilename();
    String storedFileName = UUID.randomUUID().toString() + "_" + originalFilename;
    Path destinationFile = rootLocation.resolve(storedFileName).normalize().toAbsolutePath();
    file.transferTo(destinationFile);

    TaskAttachment attachment = new TaskAttachment();
    attachment.setTaskId(taskId);
    attachment.setFileName(originalFilename);
    attachment.setStoredFileName(storedFileName);
    attachment.setContentType(file.getContentType());
    attachment.setSize(file.getSize());
    attachment.setUploadedAt(LocalDateTime.now());
    attachment.setTask(task);

    TaskAttachment saved = attachmentRepository.save(attachment);
    return toResponseDto(saved);
  }

  public Resource loadAsResource(Long attachmentId) throws MalformedURLException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new RuntimeException("Attachment not found"));
    Path file = rootLocation.resolve(attachment.getStoredFileName());
    Resource resource = new UrlResource(file.toUri());
    if (resource.exists() && resource.isReadable()) {
      return resource;
    } else {
      throw new RuntimeException("Could not read file: " + attachment.getStoredFileName());
    }
  }

  @Transactional
  public void deleteAttachment(Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new RuntimeException("Attachment not found"));
    Path file = rootLocation.resolve(attachment.getStoredFileName());
    Files.deleteIfExists(file);
    attachmentRepository.deleteById(attachmentId);
  }

  public List<AttachmentResponseDto> getAttachmentsByTaskId(Long taskId) {
    if (!taskRepository.existsById(taskId)) {
      throw new TaskNotFoundException(taskId);
    }
    return attachmentRepository.findByTaskId(taskId).stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
  }

  private AttachmentResponseDto toResponseDto(TaskAttachment attachment) {
    AttachmentResponseDto dto = new AttachmentResponseDto();
    dto.setId(attachment.getId());
    dto.setFileName(attachment.getFileName());
    dto.setSize(attachment.getSize());
    dto.setUploadedAt(attachment.getUploadedAt());
    return dto;
  }
}