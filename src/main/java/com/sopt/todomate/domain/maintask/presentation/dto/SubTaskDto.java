package com.sopt.todomate.domain.maintask.presentation.dto;

import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record SubTaskDto(
	String content,
	boolean completed
) {
	public SubTask toEntity() {
		return SubTask.createDefaultSubTask(this.content, null);
	}
}
