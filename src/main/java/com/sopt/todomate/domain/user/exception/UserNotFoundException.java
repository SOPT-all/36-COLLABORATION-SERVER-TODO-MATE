package com.sopt.todomate.domain.user.exception;

import static com.sopt.todomate.global.common.exception.constant.ExceptionCode.*;

import com.sopt.todomate.global.common.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
	public UserNotFoundException() {
		super(USER_NOT_FOUND);
	}
}
