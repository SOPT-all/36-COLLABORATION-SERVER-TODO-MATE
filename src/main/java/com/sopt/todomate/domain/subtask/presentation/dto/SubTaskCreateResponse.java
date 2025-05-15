package com.sopt.todomate.domain.subtask.presentation.dto;

import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

public record SubTaskCreateResponse(
	long id,
	String content,
	boolean completed,
	long mainTaskId
) {
	public static SubTaskCreateResponse of(SubTask subTask) {
		return new SubTaskCreateResponse(
			subTask.getId(),
			subTask.getContent(),
			subTask.getCompleted(),
			subTask.getMainTask().getId()
		);
	}
}
