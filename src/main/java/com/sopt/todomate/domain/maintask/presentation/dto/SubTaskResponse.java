package com.sopt.todomate.domain.maintask.presentation.dto;

import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record SubTaskResponse(
	long subTaskId,
	long mainTaskId,
	String content,
	boolean completed
) {
	public static SubTaskResponse from(SubTask subTask) {
		return new SubTaskResponse(
			subTask.getId(),
			subTask.getMainTask().getId(),
			subTask.getContent(),
			subTask.isCompleted()
		);
	}
}
