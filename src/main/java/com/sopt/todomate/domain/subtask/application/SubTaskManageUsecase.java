package com.sopt.todomate.domain.subtask.application;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskGetService;
import com.sopt.todomate.domain.maintask.exception.AccessDeniedException;
import com.sopt.todomate.domain.subtask.application.dto.SubTaskCompletedCommand;
import com.sopt.todomate.domain.subtask.application.dto.SubTaskCreateCommand;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskGetService;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskSaveService;
import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCreateResponse;
import com.sopt.todomate.domain.user.domain.entity.User;
import com.sopt.todomate.domain.user.domain.service.UserGetService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubTaskManageUsecase {
	private final UserGetService userGetService;
	private final SubTaskSaveService subTaskSaveService;
	private final MainTaskGetService mainTaskGetService;
	private final SubTaskGetService subTaskGetService;

	@Transactional
	public SubTaskCreateResponse createSubTask(long userId, long mainTaskId, SubTaskCreateCommand command) {
		User user = userGetService.findByUserId(userId);

		MainTask mainTask = checkAuthorityByMainTaskId(mainTaskId, user);

		SubTask subTask = SubTask.createDefaultSubTask(command.content(), mainTask);

		SubTask savedSubTask = subTaskSaveService.save(subTask);

		return SubTaskCreateResponse.of(savedSubTask);
	}

	@Transactional
	public void updateCompleted(long userId, long subTaskId, SubTaskCompletedCommand command) {
		User user = userGetService.findByUserId(userId);

		SubTask subTask = checkAuthorityBySubTaskId(subTaskId, user);

		subTask.updateCompleted(command.completed());

	}

	private MainTask checkAuthorityByMainTaskId(long mainTaskId, User user) {
		MainTask mainTask = mainTaskGetService.findByMainTaskId(mainTaskId);

		if (!mainTask.isAuthor(user)) {
			throw new AccessDeniedException();
		}

		return mainTask;

	}

	private SubTask checkAuthorityBySubTaskId(long subTaskId, User user) {
		SubTask subTask = subTaskGetService.findSubTaskById(subTaskId);
		MainTask mainTask = subTask.getMainTask();

		if (!mainTask.isAuthor(user)) {
			throw new AccessDeniedException();
		}

		return subTask;

	}
}
