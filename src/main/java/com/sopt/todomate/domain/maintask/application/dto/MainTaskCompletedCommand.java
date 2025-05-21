package com.sopt.todomate.domain.maintask.application.dto;

import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCompletedRequest;

public record MainTaskCompletedCommand (
	Boolean completed
) {
	public static MainTaskCompletedCommand from(MainTaskCompletedRequest request) {
		return new MainTaskCompletedCommand(request.completed());
	}
}
