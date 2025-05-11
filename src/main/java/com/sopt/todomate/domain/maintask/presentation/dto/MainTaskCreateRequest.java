package com.sopt.todomate.domain.maintask.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MainTaskCreateRequest(String taskContent,
									LocalDateTime startAt,
									LocalDateTime endAt,
									String routineType,
									long priority,
									String category,
									LocalDateTime taskDate,
									boolean completed,
									List<SubTaskDto> subTasks) {
}
