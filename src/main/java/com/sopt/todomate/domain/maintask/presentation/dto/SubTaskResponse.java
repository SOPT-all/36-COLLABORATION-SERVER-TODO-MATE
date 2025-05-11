package com.sopt.todomate.domain.maintask.presentation.dto;

import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record SubTaskResponse(
	long subTaskId,
	String content,
	boolean completed
) {
	public static SubTaskResponse from(SubTask subTask) {
		return new SubTaskResponse(
			subTask.getId(),
			subTask.getContent(),
			subTask.isCompleted()
		);
	}
}
