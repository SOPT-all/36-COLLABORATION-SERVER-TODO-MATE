package com.sopt.todomate.domain.maintask.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.entity.RoutineType;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record MainTaskCreateResponse(
	long maintaskId,
	String taskContent,
	LocalDateTime startAt,
	LocalDateTime endAt,
	RoutineType routineType,
	long priority,
	String category,
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
			mainTask.getRoutinCycle(),
			mainTask.getPriority(),
			mainTask.getCategory(),
			mainTask.getTaskDate(),
			mainTask.isCompleted(),
			subTaskResponses
		);
	}
}
