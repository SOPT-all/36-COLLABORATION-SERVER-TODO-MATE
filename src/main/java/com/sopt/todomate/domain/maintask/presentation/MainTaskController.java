package com.sopt.todomate.domain.maintask.presentation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopt.todomate.domain.maintask.application.dto.MainTaskCommand;
import com.sopt.todomate.domain.maintask.application.dto.MainTaskUpdateCommand;
import com.sopt.todomate.domain.maintask.application.usecase.MainTaskManageUsecase;
import com.sopt.todomate.domain.maintask.application.usecase.MainTaskQueryUsecase;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateRequest;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateResponse;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskDetailResponse;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskUpdateRequest;
import com.sopt.todomate.global.common.dto.ResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/main-tasks")
@RequiredArgsConstructor
public class MainTaskController {
	private final MainTaskManageUsecase mainTaskManageUsecase;
	private final MainTaskQueryUsecase mainTaskQueryUsecase;

	@PostMapping()
	public ResponseDto<MainTaskCreateResponse> createMainTask(@RequestHeader Long userId,
		@Valid @RequestBody MainTaskCreateRequest request) {
		MainTaskCreateResponse response = mainTaskManageUsecase.createMainTask(MainTaskCommand.from(request), userId);
		return ResponseDto.created(response);
	}

	@GetMapping("/detail")
	public ResponseDto<List<MainTaskDetailResponse>> getTodosByDate(
		@RequestHeader Long userId,
		@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
	) {
		List<MainTaskDetailResponse> response = mainTaskQueryUsecase.getTodosByDate(userId, date);
		return ResponseDto.ok(response);
	}

	@PutMapping("/{taskId}")
	public ResponseDto<Void> update(@RequestHeader Long userId, @PathVariable Long taskId,
		@Valid @RequestBody MainTaskUpdateRequest request) {
		mainTaskManageUsecase.update(userId, taskId, MainTaskUpdateCommand.from(request));
		return ResponseDto.noContent();
	}

	@DeleteMapping("/{taskId}")
	public ResponseDto<Void> delete(@RequestHeader Long userId, @PathVariable Long taskId) {
		mainTaskManageUsecase.delete(userId, taskId);
		return ResponseDto.noContent();
	}

	@DeleteMapping()
	public ResponseDto<Void> deleteAllByDate(@RequestHeader Long userId,
		@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

		mainTaskManageUsecase.deleteAllInDate(userId, date);
		return ResponseDto.noContent();
	}

	@DeleteMapping("/all")
	public ResponseDto<Void> deleteAll(@RequestHeader Long userId) {
		mainTaskManageUsecase.deleteAll(userId);
		return ResponseDto.noContent();
	}
}
