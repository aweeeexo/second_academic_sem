package com.mipt.todolistmanager.mapper;

import com.mipt.todolistmanager.dto.TaskCreateDto;
import com.mipt.todolistmanager.dto.TaskResponseDto;
import com.mipt.todolistmanager.dto.TaskUpdateDto;
import com.mipt.todolistmanager.model.Priority;
import com.mipt.todolistmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
class TaskMapperTest {

  private TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

  private TaskCreateDto createDto;
  private TaskUpdateDto updateDto;
  private Task task;
  private Set<String> tags;

  @BeforeEach
  void setUp() {
    tags = new HashSet<>();
    tags.add("work");
    tags.add("important");

    createDto = new TaskCreateDto();
    createDto.setTitle("Test Task");
    createDto.setDescription("Test Description");
    createDto.setDueDate(LocalDate.of(2024, 12, 31));
    createDto.setPriority(Priority.HIGH);
    createDto.setTags(tags);

    updateDto = new TaskUpdateDto();
    updateDto.setTitle("Updated Task");
    updateDto.setDescription("Updated Description");
    updateDto.setCompleted(true);
    updateDto.setDueDate(LocalDate.of(2024, 12, 31));
    updateDto.setPriority(Priority.LOW);

    task = new Task();
    task.setId(1L);
    task.setTitle("Original Task");
    task.setDescription("Original Description");
    task.setCompleted(false);
    task.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));
    task.setDueDate(LocalDate.of(2024, 6, 30));
    task.setPriority(Priority.MEDIUM);
    task.setTags(new HashSet<>());
  }

  @Test
  void toEntity_ShouldMapCreateDtoToTask() {
    Task result = taskMapper.toEntity(createDto);

    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo("Test Task");
    assertThat(result.getDescription()).isEqualTo("Test Description");
    assertThat(result.getDueDate()).isEqualTo(LocalDate.of(2024, 12, 31));
    assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
    assertThat(result.getTags()).containsExactlyInAnyOrder("work", "important");
    assertThat(result.getId()).isNull();
  }

  @Test
  void updateEntity_ShouldUpdateOnlyNonNullFields() {
    Task result = taskMapper.updateEntity(updateDto, task);

    assertThat(result.getTitle()).isEqualTo("Updated Task");
    assertThat(result.getDescription()).isEqualTo("Updated Description");
    assertThat(result.isCompleted()).isTrue();
    assertThat(result.getDueDate()).isEqualTo(LocalDate.of(2024, 12, 31));
    assertThat(result.getPriority()).isEqualTo(Priority.LOW);
    assertThat(result.getId()).isEqualTo(1);
    assertThat(result.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
  }

  @Test
  void updateEntity_WithNullFields_ShouldNotUpdateOriginalFields() {
    TaskUpdateDto emptyUpdate = new TaskUpdateDto();

    Task result = taskMapper.updateEntity(emptyUpdate, task);

    assertThat(result.getTitle()).isEqualTo("Original Task");
    assertThat(result.getDescription()).isEqualTo("Original Description");
    assertThat(result.isCompleted()).isFalse();
    assertThat(result.getDueDate()).isEqualTo(LocalDate.of(2024, 6, 30));
    assertThat(result.getPriority()).isEqualTo(Priority.MEDIUM);
    assertThat(result.getId()).isEqualTo(1);
  }

  @Test
  void toResponseDto_ShouldMapTaskToResponseDto() {
    Task task = new Task();
    task.setId(1L);
    task.setTitle("Response Task");
    task.setDescription("Response Description");
    task.setCompleted(true);
    task.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));
    task.setDueDate(LocalDate.of(2024, 12, 31));
    task.setPriority(Priority.HIGH);
    task.setTags(tags);

    TaskResponseDto result = taskMapper.toResponseDto(task);

    assertThat(result.getId()).isEqualTo(1);
    assertThat(result.getTitle()).isEqualTo("Response Task");
    assertThat(result.getDescription()).isEqualTo("Response Description");
    assertThat(result.isCompleted()).isTrue();
    assertThat(result.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
    assertThat(result.getDueDate()).isEqualTo(LocalDate.of(2024, 12, 31));
    assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
    assertThat(result.getTags()).containsExactlyInAnyOrder("work", "important");
  }

  @Test
  void toResponseDto_WithNullTask_ShouldReturnNull() {
    TaskResponseDto result = taskMapper.toResponseDto(null);
    assertThat(result).isNull();
  }
}