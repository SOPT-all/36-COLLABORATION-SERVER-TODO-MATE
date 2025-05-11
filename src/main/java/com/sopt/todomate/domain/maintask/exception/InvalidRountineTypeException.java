package com.sopt.todomate.domain.maintask.exception;

import com.sopt.todomate.global.common.exception.BusinessException;
import com.sopt.todomate.global.common.exception.constant.ExceptionCode;

public class InvalidRountineTypeException extends BusinessException {
	public InvalidRountineTypeException() {
		super(ExceptionCode.INVALID_ROUTINE_TYPE);
	}
}
