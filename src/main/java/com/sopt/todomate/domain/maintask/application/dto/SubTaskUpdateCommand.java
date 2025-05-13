package com.sopt.todomate.domain.maintask.application.dto;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record SubTaskUpdateCommand(
	long id,
	String content
) {
	public SubTask toEntity(MainTask mainTask) {
		return SubTask.createCompletedFalse(this.content, mainTask);
	}
}
