package com.sopt.todomate.domain.maintask.presentation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.Importance;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.entity.RoutineType;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record MainTaskDetailResponse(
	Long mainTaskId,
	String taskContent,
	Importance importance,
	CategoryType category,
	boolean completed,
	List<SubTaskResponse> subTasks
) {
	public static MainTaskDetailResponse from(MainTask task, List<SubTask> subTasks) {
		return new MainTaskDetailResponse(
			task.getId(),
			task.getTaskContent(),
			task.getImportance(),
			task.getCategory(),
			task.isCompleted(),
			subTasks.stream().map(SubTaskResponse::from).toList()
		);
	}
}
