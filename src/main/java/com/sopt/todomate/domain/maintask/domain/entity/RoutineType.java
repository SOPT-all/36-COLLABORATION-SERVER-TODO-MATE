package com.sopt.todomate.domain.maintask.domain.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sopt.todomate.domain.maintask.exception.InvalidRountineTypeException;

public enum RoutineType {
	NONE(0),
	DAILY(1),      // 매일 (1일)
	WEEKDAY(7),    // 매주 (7일)
	BIWEEKLY(14),  // 격주 (14일)
	MONTHLY(30),   // 매월 (30일 근사값)
	YEARLY(365);   // 매년 (365일)

	private final int days;

	RoutineType(int days) {
		this.days = days;
	}

	@JsonCreator
	public static RoutineType fromValue(String value) {
		try {
			return RoutineType.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new InvalidRountineTypeException();
		}
	}

	@JsonValue
	public int getDays() {
		return days;
	}

	public LocalDateTime getNextDate(LocalDateTime date) {
		return switch (this) {
			case NONE -> date;
			case DAILY -> date.plusDays(1);
			case WEEKDAY -> date.plusWeeks(1);
			case BIWEEKLY -> date.plusWeeks(2);
			case MONTHLY -> date.plusMonths(1);
			case YEARLY -> date.plusYears(1);
		};
	}
}
