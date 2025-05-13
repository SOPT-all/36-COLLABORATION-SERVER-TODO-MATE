package com.sopt.todomate.domain.maintask.application.dto;

import java.util.List;

import com.sopt.todomate.domain.maintask.domain.entity.Importance;

public record MainTaskUpdateCommand(
	String taskContent,
	List<SubTaskUpdateCommand> subTasks,
	Importance importance,
	boolean changeAll
) {
}
