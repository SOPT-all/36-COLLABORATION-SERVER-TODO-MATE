package com.sopt.todomate.domain.subtask.exception;

import static com.sopt.todomate.global.common.exception.constant.ExceptionCode.*;

import com.sopt.todomate.global.common.exception.BusinessException;

public class MaxSubTaskException extends BusinessException {
	public MaxSubTaskException() {
		super(MAX_SUBTASK);
	}
}
