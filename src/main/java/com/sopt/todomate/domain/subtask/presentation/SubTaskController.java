package com.sopt.todomate.domain.subtask.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopt.todomate.domain.subtask.application.SubTaskManageUsecase;
import com.sopt.todomate.domain.subtask.application.dto.SubTaskCreateCommand;
import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCreateRequest;
import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCreateResponse;
import com.sopt.todomate.global.common.dto.ResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sub-tasks")
@RequiredArgsConstructor
public class SubTaskController {
	private final SubTaskManageUsecase subTaskManageUsecase;

	@PostMapping()
	public ResponseDto<SubTaskCreateResponse> createSubTask(@RequestHeader Long userId, @RequestHeader Long taskId,
		@Valid @RequestBody SubTaskCreateRequest request) {
		SubTaskCreateResponse response = subTaskManageUsecase.createSubTask(userId, taskId,
			SubTaskCreateCommand.from(request));
		return ResponseDto.created(response);
	}
}
