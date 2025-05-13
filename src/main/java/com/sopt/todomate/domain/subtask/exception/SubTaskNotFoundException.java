package com.sopt.todomate.domain.subtask.exception;

import static com.sopt.todomate.global.common.exception.constant.ExceptionCode.*;

import com.sopt.todomate.global.common.exception.BusinessException;

public class SubTaskNotFoundException extends BusinessException {
	public SubTaskNotFoundException() {
		super(SUB_TASK_NOT_FOUND);
	}
}
