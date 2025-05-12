package com.sopt.todomate.domain.subtask.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.repository.SubTaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubTaskSaveService {
	private final SubTaskRepository subTaskRepository;

	public List<SubTask> saveAll(List<SubTask> subTasks) {
		return subTaskRepository.saveAll(subTasks);
	}
}
