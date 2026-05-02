package com.mipt.todolistmanager.service;

import com.mipt.todolistmanager.model.Priority;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskStatisticsJdbcService {
  private final JdbcTemplate jdbcTemplate;

  public TaskStatisticsJdbcService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Map<Priority, Long> getTasksCountByPriority() {
    String sql = "SELECT priority, COUNT(*) FROM tasks GROUP BY priority";
    RowMapper<Map.Entry<Priority, Long>> rowMapper = (rs, rowNum) ->
        Map.entry(Priority.valueOf(rs.getString("priority")), rs.getLong("count"));
    return jdbcTemplate.query(sql, rowMapper).stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}