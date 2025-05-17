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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "메인 태스크")
@RestController
@RequestMapping("/api/v1/main-tasks")
@RequiredArgsConstructor
public class MainTaskController {
	private final MainTaskManageUsecase mainTaskManageUsecase;
	private final MainTaskQueryUsecase mainTaskQueryUsecase;

	@PostMapping
	@Operation(summary = "메인태스크 생성")
	public ResponseDto<MainTaskCreateResponse> createMainTask(@RequestHeader Long userId,
		@Valid @RequestBody MainTaskCreateRequest request) {
		MainTaskCreateResponse response = mainTaskManageUsecase.createMainTask(MainTaskCommand.from(request), userId);
		return ResponseDto.created(response);
	}

	@GetMapping("/detail")
	@Operation(summary = "메인태스크 날짜별 조회")
	public ResponseDto<List<MainTaskDetailResponse>> getTodosByDate(
		@RequestHeader Long userId,
		@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
	) {
		List<MainTaskDetailResponse> response = mainTaskQueryUsecase.getTodosByDate(userId, date);
		return ResponseDto.ok(response);
	}

	@PutMapping("/{taskId}")
	@Operation(summary = "메인 태스크 수정", deprecated = true)
	public ResponseDto<Void> update(@RequestHeader Long userId, @PathVariable Long taskId,
		@Valid @RequestBody MainTaskUpdateRequest request) {
		mainTaskManageUsecase.update(userId, taskId, MainTaskUpdateCommand.from(request));
		return ResponseDto.noContent();
	}

	@DeleteMapping("/{taskId}")
	@Operation(summary = "메인 태스크 Id를 기준으로 삭제")
	public ResponseDto<Void> delete(@RequestHeader Long userId, @PathVariable Long taskId) {
		mainTaskManageUsecase.delete(userId, taskId);
		return ResponseDto.noContent();
	}

	@DeleteMapping()
	@Operation(summary = "날짜 별로 삭제")
	public ResponseDto<Void> deleteAllByDate(@RequestHeader Long userId,
		@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

		mainTaskManageUsecase.deleteAllInDate(userId, date);
		return ResponseDto.noContent();
	}

	@DeleteMapping("/all")
	@Operation(summary = "자신의 모든 메인태스크를 삭제")
	public ResponseDto<Void> deleteAll(@RequestHeader Long userId) {
		mainTaskManageUsecase.deleteAll(userId);
		return ResponseDto.noContent();
	}
}
