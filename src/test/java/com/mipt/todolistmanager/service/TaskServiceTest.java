package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.dto.TaskUpdateDto;
import com.mipt.todolistmanager.exception.TaskNotFoundException;
import com.mipt.todolistmanager.model.Task;
import com.mipt.todolistmanager.repository.TaskJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TaskServiceTest {

  @MockBean
  private TaskJpaRepository taskRepository;

  @Autowired
  private TaskService taskService;

  @Test
  void updateTask_WhenTaskExists_ShouldUpdateAndReturn() {
    // given
    Long taskId = 1L;
    Task existingTask = new Task();
    existingTask.setId(taskId);
    existingTask.setTitle("Old Title");
    existingTask.setCreatedAt(LocalDateTime.now());
    existingTask.setCompleted(false);

    TaskUpdateDto updateDto = new TaskUpdateDto();
    updateDto.setTitle("New Title");
    updateDto.setCompleted(true);

    when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
    when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // when
    taskService.updateTask(taskId, updateDto);

    // then
    ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
    verify(taskRepository).save(taskCaptor.capture());
    Task savedTask = taskCaptor.getValue();
    assertThat(savedTask.getTitle()).isEqualTo("New Title");
    assertThat(savedTask.isCompleted()).isTrue();
    verify(taskRepository).findById(taskId);
  }

  @Test
  void updateTask_WhenTaskNotFound_ShouldThrowException() {
    // given
    Long taskId = 999L;
    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> taskService.updateTask(taskId, new TaskUpdateDto()))
        .isInstanceOf(TaskNotFoundException.class);
    verify(taskRepository, never()).save(any());
  }
}