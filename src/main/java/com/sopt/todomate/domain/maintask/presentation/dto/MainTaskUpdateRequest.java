package com.sopt.todomate.domain.maintask.presentation.dto;

import java.util.List;

import com.sopt.todomate.domain.maintask.application.dto.SubTaskUpdateCommand;
import com.sopt.todomate.domain.maintask.domain.entity.Importance;

public record MainTaskUpdateRequest(
	String taskContent,
	List<SubTaskUpdateCommand> subTasks,
	Importance importance,
	boolean changeAll
) {
}
