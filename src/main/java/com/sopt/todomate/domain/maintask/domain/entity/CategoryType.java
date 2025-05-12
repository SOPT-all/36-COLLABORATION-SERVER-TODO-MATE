package com.sopt.todomate.domain.maintask.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sopt.todomate.domain.maintask.exception.InvalidCategoryTypeException;

public enum CategoryType {
	CATEGORY1("카테고리1"),
	CATEGORY2("카테고리2"),
	CATEGORY3("카테고리3");

	private final String label;

	CategoryType(String label) {
		this.label = label;
	}

	@JsonCreator
	public static CategoryType fromValue(String value) {

		for (CategoryType type : values()) {
			if (type.label.equals(value)) return type;
		}
		throw new InvalidCategoryTypeException();
	}

	@JsonValue
	public String getLabel() {
		return label;
	}
}
