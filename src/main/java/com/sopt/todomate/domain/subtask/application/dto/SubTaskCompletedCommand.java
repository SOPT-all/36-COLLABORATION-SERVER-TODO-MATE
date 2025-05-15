package com.sopt.todomate.domain.subtask.application.dto;

import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCompletedRequest;

public record SubTaskCompletedCommand(
	Boolean completed
) {
	public static SubTaskCompletedCommand from(SubTaskCompletedRequest request) {
		return new SubTaskCompletedCommand(
			request.completed()
		);
	}
}
