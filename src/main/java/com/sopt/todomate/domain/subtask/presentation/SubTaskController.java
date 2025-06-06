package com.sopt.todomate.domain.subtask.presentation;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopt.todomate.domain.subtask.application.SubTaskManageUsecase;
import com.sopt.todomate.domain.subtask.application.dto.SubTaskCompletedCommand;
import com.sopt.todomate.domain.subtask.application.dto.SubTaskCreateCommand;
import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCompletedRequest;
import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCreateRequest;
import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCreateResponse;
import com.sopt.todomate.global.common.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "서브태스크")
@RestController
@RequestMapping("/api/v1/sub-tasks")
@RequiredArgsConstructor
public class SubTaskController {
	private final SubTaskManageUsecase subTaskManageUsecase;

	@PostMapping
	@Operation(summary = "서브태스크를 생성합니다.")
	public ResponseDto<SubTaskCreateResponse> createSubTask(@RequestHeader Long userId, @RequestHeader Long taskId,
		@Valid @RequestBody SubTaskCreateRequest request) {
		SubTaskCreateResponse response = subTaskManageUsecase.createSubTask(userId, taskId,
			SubTaskCreateCommand.from(request));
		return ResponseDto.created(response);
	}

	@PatchMapping
	@Operation(summary = "서브태스크의 완료여부를 업데이트 합니다.")
	public ResponseDto<Void> updateSubTask(@RequestHeader Long userId, @RequestHeader Long taskId,
		@Valid @RequestBody SubTaskCompletedRequest request) {
		subTaskManageUsecase.updateCompleted(userId, taskId,
			SubTaskCompletedCommand.from(request));
		return ResponseDto.noContent();
	}
}
