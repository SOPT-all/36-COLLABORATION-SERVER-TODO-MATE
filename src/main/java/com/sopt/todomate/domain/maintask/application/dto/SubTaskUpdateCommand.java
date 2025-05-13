package com.sopt.todomate.domain.maintask.application.dto;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record SubTaskUpdateCommand(
	String content,
	boolean completed
) {
	public SubTask toEntity(MainTask mainTask, boolean completed) {
		return SubTask.create(this.content, mainTask, completed);
	}
}
