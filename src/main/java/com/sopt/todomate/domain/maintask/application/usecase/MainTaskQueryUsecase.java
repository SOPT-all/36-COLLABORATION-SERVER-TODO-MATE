package com.sopt.todomate.domain.maintask.application.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskGetService;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskDetailResponse;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskGetService;
import com.sopt.todomate.domain.user.domain.service.UserGetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainTaskQueryUsecase {

	private final MainTaskGetService mainTaskGetService;
	private final SubTaskGetService subTaskGetService;
	private final UserGetService userGetService;

	public List<MainTaskDetailResponse> getTodosByDate(Long userId, LocalDate date) {
		userGetService.findByUserId(userId);

		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();

		List<MainTask> mainTasks = mainTaskGetService.findAllByUserIdAndDateRange(userId, start, end);
		List<Long> mainTaskIds = mainTasks.stream().map(MainTask::getId).toList();
		List<SubTask> allSubTasks = subTaskGetService.findAllByMainTaskIds(mainTaskIds);

		Map<Long, List<SubTask>> subTasksByMainTaskId = allSubTasks.stream()
			.collect(Collectors.groupingBy(sub -> sub.getMainTask().getId()));

		return mainTasks.stream()
			.map(task -> MainTaskDetailResponse.from(task, subTasksByMainTaskId.getOrDefault(task.getId(), List.of())))
			.toList();

	}
}
