package com.sopt.todomate.domain.subtask.domain.service;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.subtask.domain.repository.SubTaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubTaskUpdateService {
	private final SubTaskRepository subTaskRepository;

	public void updateSubTask() {

	}
}
