package com.mipt.todolistmanager.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {
  @Override
  public String convertToDatabaseColumn(Set<String> attribute) {
    return attribute == null ? null : String.join(",", attribute);
  }
  @Override
  public Set<String> convertToEntityAttribute(String dbData) {
    return dbData == null ? null : Arrays.stream(dbData.split(",")).collect(Collectors.toSet());
  }
}