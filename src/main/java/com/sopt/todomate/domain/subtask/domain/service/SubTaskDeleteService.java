package com.sopt.todomate.domain.subtask.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.subtask.domain.repository.SubTaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubTaskDeleteService {
	private final SubTaskRepository subTaskRepository;

	public void deleteAllByMainTask(MainTask mainTask) {
		subTaskRepository.deleteAllByMainTask(mainTask);
	}

	public void deleteAllByMainTasks(List<MainTask> mainTasks) {
		subTaskRepository.deleteAllByMainTaskIn(mainTasks);
	}
}
