package com.sopt.todomate.domain.maintask.application.usecase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.entity.RoutineType;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskSaveService;
import com.sopt.todomate.domain.maintask.exception.EmptyRoutineDateException;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateRequest;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateResponse;
import com.sopt.todomate.domain.maintask.presentation.dto.SubTaskDto;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskSaveService;
import com.sopt.todomate.domain.user.domain.entity.User;
import com.sopt.todomate.domain.user.domain.service.UserGetService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainTaskManageUsecase {
	private final UserGetService userGetService;
	private final MainTaskSaveService mainTaskSaveService;
	private final SubTaskSaveService subTaskSaveService;

	@Transactional
	public MainTaskCreateResponse execute(MainTaskCreateRequest request, long userId) {
		User user = userGetService.findByUserId(userId);
		LocalDateTime taskDate = request.taskDate();

		MainTask firstMainTask = createAndSaveMainTask(request, user, taskDate);
		List<SubTask> firstSubTasks = createAndSaveSubTasks(request.subTasks(), firstMainTask);

		if (isRecurringTask(request)) {
			List<LocalDateTime> additionalDates = calculateAdditionalDates(request.startAt(), request.endAt(),
				request.routineType());

			for (LocalDateTime date : additionalDates) {
				MainTask additionalTask = createAndSaveMainTask(request, user, date);
				createAndSaveSubTasks(request.subTasks(), additionalTask);
			}
		}

		return MainTaskCreateResponse.from(firstMainTask, firstSubTasks);
	}

	private MainTask createAndSaveMainTask(MainTaskCreateRequest request, User user, LocalDateTime taskDate) {
		MainTask mainTask = MainTask.builder()
			.taskContent(request.taskContent())
			.startAt(request.startAt())
			.endAt(request.endAt())
			.routineType(request.routineType())
			.priority(request.priority())
			.category(request.category())
			.taskDate(taskDate)
			.user(user)
			.completed(request.completed())
			.build();

		return mainTaskSaveService.save(mainTask);
	}

	private List<SubTask> createAndSaveSubTasks(List<SubTaskDto> subTaskDtos, MainTask mainTask) {
		if (subTaskDtos == null || subTaskDtos.isEmpty()) {
			return Collections.emptyList();
		}

		List<SubTask> subTasks = subTaskDtos.stream()
			.map(dto -> SubTask.builder()
				.content(dto.content())
				.completed(dto.completed())
				.mainTask(mainTask)
				.build())
			.collect(Collectors.toList());

		return subTaskSaveService.saveAll(subTasks);
	}

	private List<LocalDateTime> calculateAdditionalDates(
		LocalDateTime startAt, LocalDateTime endAt, RoutineType routineType) {

		if (startAt == null || endAt == null || routineType == null) {
			throw new EmptyRoutineDateException();
		}

		List<LocalDateTime> dates = new ArrayList<>();
		LocalDateTime currentDate = routineType.getNextDate(startAt);

		while (!currentDate.isAfter(endAt)) {
			dates.add(currentDate);
			currentDate = routineType.getNextDate(currentDate);
		}

		return dates;
	}

	private boolean isRecurringTask(MainTaskCreateRequest request) {
		return request.routineType() != RoutineType.NONE;
	}
}
