package com.sopt.todomate.domain.maintask.application.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.RoutineType;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateRequest;

public record MainTaskCommand(
	String taskContent,
	LocalDateTime startAt,
	LocalDateTime endAt,
	RoutineType routineType,
	long priority,
	CategoryType category,
	LocalDateTime taskDate,
	boolean completed,
	List<SubTaskCommand> subTasks
) {
	public static MainTaskCommand from(MainTaskCreateRequest request) {
		List<SubTaskCommand> subTaskCommands = request.subTasks() == null ?
			Collections.emptyList() :
			request.subTasks().stream()
				.map(SubTaskCommand::from)
				.collect(Collectors.toList());

		return new MainTaskCommand(
			request.taskContent(),
			request.startAt(),
			request.endAt(),
			request.routineType(),
			request.priority(),
			request.category(),
			request.taskDate(),
			request.completed(),
			subTaskCommands
		);
	}
}
