package com.sopt.todomate.domain.maintask.presentation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record MainTaskDetailResponse(
	Long mainTaskId,
	String taskContent,
	LocalDateTime startAt,
	LocalDateTime endAt,
	String routineType,
	int priority,
	String category,
	LocalDate taskDate,
	boolean completed,
	List<SubTaskResponse> subTasks
) {
	public static MainTaskDetailResponse from(MainTask task, List<SubTask> subTasks) {
		return new MainTaskDetailResponse(
			task.getId(),
			task.getTaskContent(),
			task.getStartAt(),
			task.getEndAt(),
			task.getRoutineType().name(),
			task.getImportance().getValue(),
			task.getCategory().getLabel(),
			task.getTaskDate().toLocalDate(),
			task.isCompleted(),
			subTasks.stream().map(SubTaskResponse::from).toList()
		);
	}
}
