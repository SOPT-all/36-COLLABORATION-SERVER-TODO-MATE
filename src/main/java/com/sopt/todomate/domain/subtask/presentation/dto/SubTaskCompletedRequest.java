package com.sopt.todomate.domain.subtask.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record SubTaskCompletedRequest(
	@NotNull
	Boolean completed
) {
}
