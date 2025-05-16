package com.sopt.todomate.domain.maintask.application.usecase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.application.dto.MainTaskCommand;
import com.sopt.todomate.domain.maintask.application.dto.MainTaskUpdateCommand;
import com.sopt.todomate.domain.maintask.application.dto.SubTaskUpdateCommand;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskDeleteService;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskGetService;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskSaveService;
import com.sopt.todomate.domain.maintask.exception.AccessDeniedException;
import com.sopt.todomate.domain.maintask.exception.MaxMainTaskException;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateResponse;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskDeleteService;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskSaveService;
import com.sopt.todomate.domain.subtask.exception.MaxSubTaskException;
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
	private final SubTaskDeleteService subTaskDeleteService;
	private final MainTaskDeleteService mainTaskDeleteService;

	@Transactional
	public MainTaskCreateResponse createMainTask(MainTaskCommand command, long userId) {

		User user = userGetService.findByUserId(userId);

		if (mainTaskGetService.findAmountByCategory(user, command.category(), command.taskDate()) >= 2) {
			throw new MaxMainTaskException();
		}

		MainTask mainTask = MainTask.createMainTaskWithoutRoutine(command.taskContent(), command.category(),
			command.taskDate(), user);

		mainTaskSaveService.save(mainTask);

		return MainTaskCreateResponse.from(mainTask);
	}

	@Transactional
	public void update(long userId, long mainTaskId, MainTaskUpdateCommand command) {
		User user = userGetService.findByUserId(userId);
		MainTask mainTask = checkAuthorityByMainTaskId(mainTaskId, user);
		mainTask.updateContent(command.taskContent());
		mainTask.updateImportance(command.importance());
		updateSubTasks(mainTask, command.subTasks());

		if (command.changeAll()) {
			List<MainTask> mainTasks = mainTaskGetService.findAllByTemplateIdAndAfterDate(mainTask.getTemplateTaskId(),
				mainTask.getTaskDate());

			for (MainTask routineTask : mainTasks) {
				routineTask.updateContent(command.taskContent());
				routineTask.updateImportance(command.importance());
				subTaskDeleteService.deleteAllByMainTask(routineTask);

				List<SubTask> subTasks = command.subTasks().stream()
					.map(updateCommand -> updateCommand.toEntity(routineTask, false))
					.toList();

				subTaskSaveService.saveAll(subTasks);
			}
		}
	}

	@Transactional
	public void delete(long userId, long mainTaskId) {
		User user = userGetService.findByUserId(userId);

		MainTask mainTask = checkAuthorityByMainTaskId(mainTaskId, user);

		subTaskDeleteService.deleteAllByMainTask(mainTask);

		mainTaskDeleteService.delete(mainTask);
	}

	@Transactional
	public void deleteAllInDate(long userId, LocalDate taskDate) {
		User user = userGetService.findByUserId(userId);

		List<MainTask> mainTasks = mainTaskGetService.findALlByUserAndDate(user, taskDate);

		subTaskDeleteService.deleteAllByMainTasks(mainTasks);

		mainTaskDeleteService.deleteAll(mainTasks);
	}

	@Transactional
	public void deleteAll(long userId) {
		User user = userGetService.findByUserId(userId);

		List<MainTask> mainTasks = mainTaskGetService.findAllByUser(user);

		subTaskDeleteService.deleteAllByMainTasks(mainTasks);

		mainTaskDeleteService.deleteAll(mainTasks);
	}

	private MainTask checkAuthorityByMainTaskId(long mainTaskId, User user) {
		MainTask mainTask = mainTaskGetService.findByMainTaskId(mainTaskId);

		if (!mainTask.isAuthor(user)) {
			throw new AccessDeniedException();
		}

		return mainTask;

	}

	private void updateSubTasks(MainTask mainTask, List<SubTaskUpdateCommand> subTaskCommands) {
		if (subTaskCommands.size() > 3) {
			throw new MaxSubTaskException();
		}
		subTaskDeleteService.deleteAllByMainTask(mainTask);

		List<SubTask> subTasks = subTaskCommands.stream()
			.map(updateCommand -> updateCommand.toEntity(mainTask, updateCommand.completed()))
			.toList();

		subTaskSaveService.saveAll(subTasks);
	}
}
