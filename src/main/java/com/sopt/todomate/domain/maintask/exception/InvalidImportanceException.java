package com.sopt.todomate.domain.maintask.exception;

import static com.sopt.todomate.global.common.exception.constant.ExceptionCode.*;

import com.sopt.todomate.global.common.exception.BusinessException;

public class InvalidImportanceException extends BusinessException {
	public InvalidImportanceException() {
		super(INVALID_IMPORTANCE);
	}
}
