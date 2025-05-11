package com.sopt.todomate.domain.maintask.domain.entity;

import lombok.Getter;

@Getter
public enum RoutineCycle {
	DAILY("매일"),
	WEEKDAY("매주"),
	BIWEEKLY("격주"),
	MONTHLY("매월"),
	YEARLY("매년");

	private final String koreanValue;

	RoutineCycle(String koreanValue) {
		this.koreanValue = koreanValue;
	}

}