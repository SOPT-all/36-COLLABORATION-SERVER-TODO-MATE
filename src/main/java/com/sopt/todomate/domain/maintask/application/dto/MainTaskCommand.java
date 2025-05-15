package com.sopt.todomate.domain.maintask.application.dto;

import java.time.LocalDateTime;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateRequest;

public record MainTaskCommand(
	String taskContent,
	CategoryType category,
	LocalDateTime taskDate
) {
	public static MainTaskCommand from(MainTaskCreateRequest request) {
		return new MainTaskCommand(
			request.taskContent(),
			request.category(),
			request.taskDate()
		);
	}
}
