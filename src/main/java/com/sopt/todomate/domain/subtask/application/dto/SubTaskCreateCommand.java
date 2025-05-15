package com.sopt.todomate.domain.subtask.application.dto;

import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCreateRequest;

public record SubTaskCreateCommand(
	String content
) {
	public static SubTaskCreateCommand from(SubTaskCreateRequest request) {
		return new SubTaskCreateCommand(request.content());
	}
}
