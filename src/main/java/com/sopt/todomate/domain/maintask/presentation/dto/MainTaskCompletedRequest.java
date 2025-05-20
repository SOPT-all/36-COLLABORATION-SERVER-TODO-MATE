package com.sopt.todomate.domain.maintask.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record MainTaskCompletedRequest(
	@NotNull Boolean completed
) {
}
