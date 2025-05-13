package com.sopt.todomate.domain.maintask.application.usecase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.application.dto.MainTaskCommand;
import com.sopt.todomate.domain.maintask.application.dto.MainTaskUpdateCommand;
import com.sopt.todomate.domain.maintask.application.dto.SubTaskCommand;
import com.sopt.todomate.domain.maintask.application.dto.SubTaskUpdateCommand;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.entity.RoutineType;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskGetService;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskSaveService;
import com.sopt.todomate.domain.maintask.exception.EmptyRoutineDateException;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateResponse;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskGetService;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskSaveService;
import com.sopt.todomate.domain.subtask.exception.SubTaskNotIncludeException;
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
	private final MainTaskGetService mainTaskGetService;
	private final SubTaskGetService subTaskGetService;

	@Transactional
	public MainTaskCreateResponse execute(MainTaskCommand command, long userId) {

		User user = userGetService.findByUserId(userId);
		LocalDateTime taskDate = command.taskDate();

		MainTask firstMainTask = createAndSaveMainTask(command, user, taskDate);
		firstMainTask.updateTemplateTask(firstMainTask.getId());
		List<SubTask> firstSubTasks = createAndSaveSubTasks(command.subTasks(), firstMainTask);

		if (isRecurringTask(command)) {
			List<LocalDateTime> additionalDates = calculateAdditionalDates(
				command.startAt(), command.endAt(), command.routineType());

			for (LocalDateTime date : additionalDates) {
				MainTask additionalTask = createAndSaveMainTask(command, user, date);
				additionalTask.updateTemplateTask(firstMainTask.getId());
				createAndSaveSubTasks(command.subTasks(), additionalTask);
			}
		}

		return MainTaskCreateResponse.from(firstMainTask, firstSubTasks);
	}

	private MainTask createAndSaveMainTask(MainTaskCommand command, User user, LocalDateTime taskDate) {
		MainTask mainTask = MainTask.builder()
			.taskContent(command.taskContent())
			.startAt(command.startAt())
			.endAt(command.endAt())
			.routineType(command.routineType())
			.priority(command.priority())
			.category(command.category())
			.taskDate(taskDate)
			.user(user)
			.completed(command.completed())
			.build();

		return mainTaskSaveService.save(mainTask);
	}

	private List<SubTask> createAndSaveSubTasks(List<SubTaskCommand> subTaskCommands, MainTask mainTask) {
		if (subTaskCommands == null || subTaskCommands.isEmpty()) {
			return Collections.emptyList();
		}

		List<SubTask> subTasks = subTaskCommands.stream()
			.map(command -> command.toEntity(mainTask))
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

	private boolean isRecurringTask(MainTaskCommand command) {
		return command.routineType() != RoutineType.NONE;
	}

	@Transactional
	public void update(long mainTaskId, MainTaskUpdateCommand command, long userId) {
		User user = userGetService.findByUserId(userId);
		MainTask mainTask = mainTaskGetService.findByMainTaskId(mainTaskId);
		mainTask.updateContent(command.taskContent());
		updateSubTasks(mainTask, command.subTasks());
	}

	private void updateSubTasks(MainTask mainTask, List<SubTaskUpdateCommand> subTaskCommands) {
		for (SubTaskUpdateCommand subTaskCmd : subTaskCommands) {
			SubTask subTask = subTaskGetService.findSubTaskById(subTaskCmd.id());

			if (!subTask.getMainTask().getId().equals(mainTask.getId())) {
				throw new SubTaskNotIncludeException();
			}
			subTask.updateContent(subTaskCmd.content());
		}
	}
}
