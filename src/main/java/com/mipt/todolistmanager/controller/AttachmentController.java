package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.dto.AttachmentResponseDto;
import com.mipt.todolistmanager.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AttachmentController {
  private final AttachmentService attachmentService;
  @Value("${api.version}")
  private String apiVersion;

  public AttachmentController(AttachmentService attachmentService) {
    this.attachmentService = attachmentService;
  }

  @Operation(summary = "Upload attachment to a task")
  @PostMapping("/tasks/{taskId}/attachments")
  public ResponseEntity<AttachmentResponseDto> uploadAttachment(@PathVariable Long taskId,
      @RequestParam("file") MultipartFile file) throws IOException {
    AttachmentResponseDto dto = attachmentService.storeAttachment(taskId, file);
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("X-API-Version", apiVersion)
        .body(dto);
  }

  @Operation(summary = "Download an attachment")
  @GetMapping("/attachments/{attachmentId}")
  public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) throws MalformedURLException {
    Resource resource = attachmentService.loadAsResource(attachmentId);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .header("X-API-Version", apiVersion)
        .body(resource);
  }

  @Operation(summary = "Delete an attachment")
  @DeleteMapping("/attachments/{attachmentId}")
  public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) throws IOException {
    attachmentService.deleteAttachment(attachmentId);
    return ResponseEntity.noContent()
        .header("X-API-Version", apiVersion)
        .build();
  }

  @Operation(summary = "Get all attachments of a task")
  @GetMapping("/tasks/{taskId}/attachments")
  public ResponseEntity<List<AttachmentResponseDto>> getAttachments(@PathVariable Long taskId) {
    List<AttachmentResponseDto> attachments = attachmentService.getAttachmentsByTaskId(taskId);
    return ResponseEntity.ok()
        .header("X-API-Version", apiVersion)
        .body(attachments);
  }
}