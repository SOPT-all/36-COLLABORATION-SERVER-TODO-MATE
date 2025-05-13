package com.sopt.todomate.domain.maintask.presentation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopt.todomate.domain.maintask.application.dto.MainTaskCommand;
import com.sopt.todomate.domain.maintask.application.dto.MainTaskUpdateCommand;
import com.sopt.todomate.domain.maintask.application.usecase.MainTaskManageUsecase;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateRequest;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateResponse;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskUpdateRequest;
import com.sopt.todomate.global.common.dto.ResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/main-tasks")
@RequiredArgsConstructor
public class MainTaskController {
	private final MainTaskManageUsecase mainTaskManageUsecase;

	@PostMapping()
	public ResponseDto<MainTaskCreateResponse> create(@RequestHeader Long userId,
		@Valid @RequestBody MainTaskCreateRequest request) {
		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request), userId);
		return ResponseDto.created(response);
	}

	@PutMapping("/{taskId}")
	public ResponseDto<Void> create(@RequestHeader Long userId, @PathVariable Long taskId,
		@Valid @RequestBody MainTaskUpdateRequest request) {
		mainTaskManageUsecase.update(taskId, MainTaskUpdateCommand.from(request),
			userId);
		return ResponseDto.okWithoutContent();
	}
}
