package com.sopt.todomate.domain.maintask.application.dto;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.presentation.dto.SubTaskDto;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record SubTaskCommand(
	String content,
	boolean completed
) {
	public static SubTaskCommand from(SubTaskDto dto) {
		return new SubTaskCommand(
			dto.content(),
			dto.completed()
		);
	}

	public SubTask toEntity(MainTask mainTask) {
		return SubTask.createDefaultSubTask(this.content, mainTask);
	}
}
