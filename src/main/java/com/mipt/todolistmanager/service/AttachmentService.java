package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.dto.AttachmentResponseDto;
import com.mipt.todolistmanager.exception.TaskNotFoundException;
import com.mipt.todolistmanager.model.TaskAttachment;
import com.mipt.todolistmanager.repository.InMemoryTaskRepository;
import com.mipt.todolistmanager.repository.TaskAttachmentRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
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
  private final TaskAttachmentRepository attachmentRepository;
  private final InMemoryTaskRepository taskRepository;
  private final Path rootLocation = Paths.get("uploads");

  public AttachmentService(TaskAttachmentRepository attachmentRepository, InMemoryTaskRepository taskRepository) {
    this.attachmentRepository = attachmentRepository;
    this.taskRepository = taskRepository;
  }

  public AttachmentResponseDto storeAttachment(Long taskId, MultipartFile file) throws IOException {
    if (!taskRepository.existsById(Math.toIntExact(taskId))) {
      throw new TaskNotFoundException(taskId);
    }
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

    TaskAttachment saved = attachmentRepository.save(attachment);
    return toResponseDto(saved);
  }

  public Resource loadAsResource(Long attachmentId) throws MalformedURLException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId);
    if (attachment == null) {
      throw new RuntimeException("Attachment not found");
    }
    Path file = rootLocation.resolve(attachment.getStoredFileName());
    Resource resource = new UrlResource(file.toUri());
    if (resource.exists() && resource.isReadable()) {
      return resource;
    } else {
      throw new RuntimeException("Could not read file: " + attachment.getStoredFileName());
    }
  }

  public void deleteAttachment(Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId);
    if (attachment == null) {
      throw new RuntimeException("Attachment not found");
    }
    Path file = rootLocation.resolve(attachment.getStoredFileName());
    Files.deleteIfExists(file);
    attachmentRepository.deleteById(attachmentId);
  }

  public List<AttachmentResponseDto> getAttachmentsByTaskId(Long taskId) {
    if (!taskRepository.existsById(Math.toIntExact(taskId))) {
      throw new TaskNotFoundException(taskId);
    }
    return attachmentRepository.findByTaskId(taskId).values().stream()
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