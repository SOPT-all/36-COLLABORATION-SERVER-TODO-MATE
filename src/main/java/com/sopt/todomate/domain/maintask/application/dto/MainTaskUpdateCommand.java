package com.sopt.todomate.domain.maintask.application.dto;

import java.util.List;

import com.sopt.todomate.domain.maintask.domain.entity.Importance;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskUpdateRequest;

public record MainTaskUpdateCommand(
	String taskContent,
	List<SubTaskUpdateCommand> subTasks,
	Importance importance,
	boolean changeAll
) {
	public static MainTaskUpdateCommand from(MainTaskUpdateRequest request) {
		return new MainTaskUpdateCommand(
			request.taskContent(),
			request.subTasks(),
			request.importance(),
			request.changeAll()
		);
	}
}
