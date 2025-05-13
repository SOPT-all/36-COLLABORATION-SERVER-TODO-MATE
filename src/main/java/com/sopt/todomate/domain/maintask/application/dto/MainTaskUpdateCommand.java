package com.sopt.todomate.domain.maintask.application.dto;

import java.util.List;

public record MainTaskUpdateCommand(
	String taskContent,
	List<SubTaskUpdateCommand> subTasks,
	boolean changeAll
) {
}
