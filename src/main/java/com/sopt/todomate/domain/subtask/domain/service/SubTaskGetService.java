package com.sopt.todomate.domain.subtask.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.repository.MainTaskRepository;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.repository.SubTaskRepository;
import com.sopt.todomate.domain.subtask.exception.SubTaskNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubTaskGetService {
	private final SubTaskRepository subTaskRepository;
	private final MainTaskRepository mainTaskRepository;

	public List<SubTask> findAllByMainTask(MainTask mainTask) {
		return subTaskRepository.findAllByMainTask(mainTask);
	}

	public SubTask findSubTaskById(long subTaskId) {
		return subTaskRepository.findById(subTaskId).orElseThrow(SubTaskNotFoundException::new);
	}

	public List<SubTask> findAllByMainTaskIds(List<Long> mainTaskIds) {
		return subTaskRepository.findAllByMainTaskIds(mainTaskIds);
	}
}
