package com.sopt.todomate.domain.maintask.presentation.dto;

import java.time.LocalDateTime;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.Importance;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;

public record MainTaskCreateResponse(
	long mainTaskId,
	String taskContent,
	Importance importance,
	CategoryType category,
	LocalDateTime taskDate,
	boolean completed
) {
	public static MainTaskCreateResponse from(MainTask mainTask) {

		return new MainTaskCreateResponse(
			mainTask.getId(),
			mainTask.getTaskContent(),
			mainTask.getImportance(),
			mainTask.getCategory(),
			mainTask.getTaskDate(),
			mainTask.isCompleted()
		);
	}
}
