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
import com.sopt.todomate.domain.subtask.application.dto.SubTaskCreateCommand;
import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCreateRequest;
import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCreateResponse;
import com.sopt.todomate.global.common.dto.ResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/main-tasks")
@RequiredArgsConstructor
public class MainTaskController {
	private final MainTaskManageUsecase mainTaskManageUsecase;

	@PostMapping()
	public ResponseDto<MainTaskCreateResponse> createMainTask(@RequestHeader Long userId,
		@Valid @RequestBody MainTaskCreateRequest request) {
		MainTaskCreateResponse response = mainTaskManageUsecase.createMainTask(MainTaskCommand.from(request), userId);
		return ResponseDto.created(response);
	}

	@PostMapping("/{taskId}")
	public ResponseDto<SubTaskCreateResponse> createSubTask(@RequestHeader Long userId, @PathVariable Long taskId,
		@Valid @RequestBody SubTaskCreateRequest request) {
		SubTaskCreateResponse response = mainTaskManageUsecase.createSubTask(userId, taskId,
			SubTaskCreateCommand.from(request));
		return ResponseDto.created(response);
	}

	@PutMapping("/{taskId}")
	public ResponseDto<Void> update(@RequestHeader Long userId, @PathVariable Long taskId,
		@Valid @RequestBody MainTaskUpdateRequest request) {
		mainTaskManageUsecase.update(userId, taskId, MainTaskUpdateCommand.from(request));
		return ResponseDto.okWithoutContent();
	}
}
