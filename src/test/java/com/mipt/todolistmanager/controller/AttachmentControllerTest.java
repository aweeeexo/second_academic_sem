package com.mipt.todolistmanager.controller;

import com.mipt.todolistmanager.dto.AttachmentResponseDto;
import com.mipt.todolistmanager.service.AttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttachmentController.class)
@ActiveProfiles("test")
class AttachmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AttachmentService attachmentService;

  private AttachmentResponseDto responseDto;
  private MockMultipartFile mockFile;

  @BeforeEach
  void setUp() {
    responseDto = new AttachmentResponseDto();
    responseDto.setId(1L);
    responseDto.setFileName("test.txt");
    responseDto.setSize(1024);
    responseDto.setUploadedAt(LocalDateTime.now());

    mockFile = new MockMultipartFile(
        "file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "test content".getBytes()
    );
  }

  @Test
  void uploadAttachment_ShouldReturn201() throws Exception {
    when(attachmentService.storeAttachment(eq(1L), any())).thenReturn(responseDto);

    mockMvc.perform(multipart("/api/tasks/1/attachments")
            .file(mockFile))
        .andExpect(status().isCreated())
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.fileName").value("test.txt"));
  }

  @Test
  void uploadAttachment_WhenTaskNotFound_ShouldReturn404() throws Exception {
    when(attachmentService.storeAttachment(eq(999L), any()))
        .thenThrow(new RuntimeException("Task not found"));

    mockMvc.perform(multipart("/api/tasks/999/attachments")
            .file(mockFile))
        .andExpect(status().isNotFound());
  }

  @Test
  void downloadAttachment_WhenExists_ShouldReturnFile() throws Exception {
    Resource resource = new ByteArrayResource("test content".getBytes());
    when(attachmentService.loadAsResource(1L)).thenReturn(resource);

    mockMvc.perform(get("/api/attachments/1"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(header().exists("Content-Disposition"));
  }

  @Test
  void downloadAttachment_WhenNotFound_ShouldReturn404() throws Exception {
    when(attachmentService.loadAsResource(999L))
        .thenThrow(new RuntimeException("Attachment not found"));

    mockMvc.perform(get("/api/attachments/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteAttachment_WhenExists_ShouldReturn204() throws Exception {
    doNothing().when(attachmentService).deleteAttachment(1L);

    mockMvc.perform(delete("/api/attachments/1"))
        .andExpect(status().isNoContent())
        .andExpect(header().string("X-API-Version", "2.0.0"));
  }

  @Test
  void getAttachments_ShouldReturnList() throws Exception {
    List<AttachmentResponseDto> attachments = Arrays.asList(responseDto);
    when(attachmentService.getAttachmentsByTaskId(1L)).thenReturn(attachments);

    mockMvc.perform(get("/api/tasks/1/attachments"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-API-Version", "2.0.0"))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].fileName").value("test.txt"));
  }
}