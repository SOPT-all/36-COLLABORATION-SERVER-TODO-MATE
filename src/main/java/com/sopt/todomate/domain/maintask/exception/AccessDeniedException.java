package com.sopt.todomate.domain.maintask.exception;

import static com.sopt.todomate.global.common.exception.constant.ExceptionCode.*;

import com.sopt.todomate.global.common.exception.BusinessException;

public class AccessDeniedException extends BusinessException {
	public AccessDeniedException() {
		super(ACCESS_DENIED);
	}
}
