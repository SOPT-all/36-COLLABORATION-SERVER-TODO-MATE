package com.sopt.todomate.domain.maintask.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.Importance;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.entity.RoutineType;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record MainTaskCreateResponse(
	long mainTaskId,
	String taskContent,
	LocalDateTime startAt,
	LocalDateTime endAt,
	RoutineType routineType,
	Importance importance,
	CategoryType category,
	LocalDateTime taskDate,
	boolean completed,
	List<SubTaskResponse> subTasks
) {
	public static MainTaskCreateResponse from(MainTask mainTask, List<SubTask> subTasks) {
		List<SubTaskResponse> subTaskResponses = subTasks.stream()
			.map(SubTaskResponse::from)
			.toList();

		return new MainTaskCreateResponse(
			mainTask.getId(),
			mainTask.getTaskContent(),
			mainTask.getStartAt(),
			mainTask.getEndAt(),
			mainTask.getRoutineType(),
			mainTask.getImportance(),
			mainTask.getCategory(),
			mainTask.getTaskDate(),
			mainTask.isCompleted(),
			subTaskResponses
		);
	}
}
