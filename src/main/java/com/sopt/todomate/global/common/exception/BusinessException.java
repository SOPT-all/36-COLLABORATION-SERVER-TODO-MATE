package com.sopt.todomate.global.common.exception;

import com.sopt.todomate.global.common.exception.constant.ExceptionCode;

public class BusinessException extends RuntimeException {

	private final ExceptionCode exceptionCode;

	public BusinessException(ExceptionCode exceptionCode) {
		super(exceptionCode.getMessage());
		this.exceptionCode = exceptionCode;
	}

	public ExceptionCode getErrorCode() {
		return exceptionCode;
	}
}
