package com.sopt.todomate.domain.maintask.presentation.dto;

import java.util.List;

import com.sopt.todomate.domain.maintask.application.dto.SubTaskUpdateCommand;

public record MainTaskUpdateRequest(
	String taskContent,
	List<SubTaskUpdateCommand> subTasks,
	boolean changeAll
) {
}
