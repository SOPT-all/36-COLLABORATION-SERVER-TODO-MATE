package com.sopt.todomate.domain.maintask.presentation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopt.todomate.domain.maintask.application.usecase.MainTaskQueryUsecase;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskDetailResponse;
import com.sopt.todomate.global.common.dto.ResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
public class MainTaskQueryController {

	private final MainTaskQueryUsecase mainTaskQueryUsecase;

	@GetMapping("/detail")
	public ResponseDto<List<MainTaskDetailResponse>> getTodosByDate(
		@RequestHeader Long userId,
		@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
	) {
		List<MainTaskDetailResponse> response = mainTaskQueryUsecase.getTodosByDate(userId, date);
		return ResponseDto.ok(response);
	}

}
