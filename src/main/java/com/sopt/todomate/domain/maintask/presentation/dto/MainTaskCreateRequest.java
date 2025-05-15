package com.sopt.todomate.domain.maintask.presentation.dto;

import java.time.LocalDateTime;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MainTaskCreateRequest(
	@NotBlank(message = "일정 내용은 비어있을 수 없습니다.")
	String taskContent,
	@NotNull(message = "카테고리는 비어있을 수 없습니다.")
	CategoryType category,
	@NotNull(message = "일정 날짜는 비어있을 수 없습니다.")
	LocalDateTime taskDate) {
}
