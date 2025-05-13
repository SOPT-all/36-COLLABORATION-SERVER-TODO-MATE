package com.sopt.todomate.domain.maintask.application.dto;

public record SubTaskUpdateCommand(
	long id,
	String content
) {
}
