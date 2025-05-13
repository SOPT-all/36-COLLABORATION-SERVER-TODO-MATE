package com.sopt.todomate.domain.subtask.domain.service;

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
}
