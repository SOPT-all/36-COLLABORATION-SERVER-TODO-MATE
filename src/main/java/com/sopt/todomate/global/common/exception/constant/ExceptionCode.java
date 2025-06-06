package com.sopt.todomate.global.common.exception.constant;

import org.springframework.http.HttpStatus;

public enum ExceptionCode {

	//400
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "c4000", "잘못된 요청입니다."),
	EMPTY_USER_ID(HttpStatus.BAD_REQUEST, "c40010", "유저ID는 필수입니다."),
	INVALID_ROUTINE_TYPE(HttpStatus.BAD_REQUEST, "c40011", "유효하지 않은 루틴타입 입니다."),
	INVALID_CATEGORY_TYPE(HttpStatus.BAD_REQUEST, "c40012", "유효하지 않은 카테고리 타입 입니다."),
	INVALID_IMPORTANCE(HttpStatus.BAD_REQUEST, "c40013", "유효하지 않은 중요도 타입 입니다."),
	INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "c40014", "날짜 형식은 yyyy-MM-dd이어야 합니다."),
	EMPTY_ROUTINE_DATE(HttpStatus.BAD_REQUEST, "c40021", "루틴 생성시 날짜는 모두 입력되어야 합니다."),
	MAX_SUBTASK(HttpStatus.BAD_REQUEST, "c40022", "서브테스크는 6개이상 생성할 수 없습니다."),
	MAX_MAINTASK(HttpStatus.BAD_REQUEST, "c40015", "메인 태스크는 3개이상 생성할 수 없습니다."),

	//403
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "c40310", "다른 사람의 투두는 수정할 수 없습니다."),

	//404
	NOT_FOUND(HttpStatus.NOT_FOUND, "c4040", "리소스가 존재하지 않습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "c4042", "사용자가 존재하지 않습니다."),
	MAIN_TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "c4043", "메인 태스크가 존재하지 않습니다."),
	SUB_TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "c4044", "서브 태스크가 존재하지 않습니다."),
	SUB_TASK_NOT_INCLUDED(HttpStatus.NOT_FOUND, "c40441", "서브태스크가 해당 메인태스크에 속하지 않습니다."),

	//405
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "c4050", "잘못된 HTTP method 요청입니다."),

	//409
	DUPLICATE(HttpStatus.CONFLICT, "c4090", "이미 존재하는 리소스입니다."),

	//500
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "c5000", "서버 내부 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	ExceptionCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
