package com.sopt.todomate.domain.maintask.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.RoutineType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MainTaskCreateRequest(
	@NotBlank(message = "일정 내용은 비어있을 수 없습니다.")
	String taskContent,
	LocalDateTime startAt,
	LocalDateTime endAt,
	@NotNull(message = "루틴 종류는 비어있을 수 없습니다.")
	RoutineType routineType,
	long priority,
	@NotNull(message = "카테고리는 비어있을 수 없습니다.")
	CategoryType category,
	@NotNull(message = "일정 날짜는 비어있을 수 없습니다.")
	LocalDateTime taskDate,
	@NotNull(message = "완료 여부는 비어있을 수 없습니다.")
	boolean completed,
	List<SubTaskDto> subTasks) {
}
