package com.sopt.todomate.domain.maintask.exception;

import com.sopt.todomate.global.common.exception.BusinessException;
import com.sopt.todomate.global.common.exception.constant.ExceptionCode;

public class MaxMainTaskException extends BusinessException {
	public MaxMainTaskException() {
		super(ExceptionCode.MAX_MAINTASK);
	}
}
