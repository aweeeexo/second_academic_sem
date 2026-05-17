package com.mipt.todolistmanager.validator;

import com.mipt.todolistmanager.dto.TaskUpdateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DueDateNotBeforeCreationValidator implements ConstraintValidator<DueDateNotBeforeCreation, TaskUpdateDto> {
  @Override
  public boolean isValid(TaskUpdateDto dto, ConstraintValidatorContext context) {
    return true;
  }
}