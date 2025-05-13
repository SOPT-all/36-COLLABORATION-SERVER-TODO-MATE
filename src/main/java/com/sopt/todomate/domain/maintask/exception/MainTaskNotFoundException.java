package com.sopt.todomate.domain.maintask.exception;

import static com.sopt.todomate.global.common.exception.constant.ExceptionCode.*;

import com.sopt.todomate.global.common.exception.BusinessException;

public class MainTaskNotFoundException extends BusinessException {
	public MainTaskNotFoundException() {
		super(MAIN_TASK_NOT_FOUND);
	}
}
