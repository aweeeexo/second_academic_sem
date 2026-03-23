package com.mipt.todolistmanager.mapper;

import com.mipt.todolistmanager.dto.TaskCreateDto;
import com.mipt.todolistmanager.dto.TaskResponseDto;
import com.mipt.todolistmanager.dto.TaskUpdateDto;
import com.mipt.todolistmanager.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "completed", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Task toEntity(TaskCreateDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Task updateEntity(TaskUpdateDto dto, @MappingTarget Task task);

  TaskResponseDto toResponseDto(Task task);
}