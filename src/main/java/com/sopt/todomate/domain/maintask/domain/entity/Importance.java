package com.sopt.todomate.domain.maintask.domain.entity;

import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sopt.todomate.domain.maintask.exception.InvalidImportanceException;

import lombok.Getter;

@Getter
public enum Importance {
	HIGH(3, "상"),
	MEDIUM(2, "중"),
	LOW(1, "하");

	private final int value;
	private final String displayName;

	Importance(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	@JsonCreator
	public static Importance fromValue(String value) {
		try {
			return Importance.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new InvalidImportanceException();
		}
	}

	public static Comparator<Importance> comparator() {
		return Comparator.comparing(Importance::getValue).reversed();
	}

}
