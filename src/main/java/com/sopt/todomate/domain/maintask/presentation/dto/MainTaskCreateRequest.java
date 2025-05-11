package com.sopt.todomate.domain.maintask.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.sopt.todomate.domain.maintask.domain.entity.RoutineType;

public record MainTaskCreateRequest(String taskContent,
									LocalDateTime startAt,
									LocalDateTime endAt,
									RoutineType routineType,
									long priority,
									String category,
									LocalDateTime taskDate,
									boolean completed,
									List<SubTaskDto> subTasks) {
}
