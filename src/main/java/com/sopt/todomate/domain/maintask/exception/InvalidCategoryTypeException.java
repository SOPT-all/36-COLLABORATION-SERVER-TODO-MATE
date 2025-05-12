package com.sopt.todomate.domain.maintask.exception;

import com.sopt.todomate.global.common.exception.BusinessException;
import com.sopt.todomate.global.common.exception.constant.ExceptionCode;

public class InvalidCategoryTypeException extends BusinessException {
	public InvalidCategoryTypeException() {
		super(ExceptionCode.INVALID_CATEGORY_TYPE);
	}
}
