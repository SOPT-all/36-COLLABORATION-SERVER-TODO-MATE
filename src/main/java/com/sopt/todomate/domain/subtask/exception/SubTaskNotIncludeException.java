package com.sopt.todomate.domain.subtask.exception;

import com.sopt.todomate.global.common.exception.BusinessException;
import com.sopt.todomate.global.common.exception.constant.ExceptionCode;

public class SubTaskNotIncludeException extends BusinessException {
	public SubTaskNotIncludeException() {
		super(ExceptionCode.SUB_TASK_NOT_INCLUDED);
	}
}
