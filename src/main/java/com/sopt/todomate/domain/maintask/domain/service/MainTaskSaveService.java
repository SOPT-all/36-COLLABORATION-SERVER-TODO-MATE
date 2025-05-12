package com.sopt.todomate.domain.maintask.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.repository.MainTaskRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainTaskSaveService {
	private final MainTaskRepository mainTaskRepository;

	@Transactional
	public MainTask save(MainTask mainTask) {
		return mainTaskRepository.save(mainTask);
	}

	@Transactional
	public List<MainTask> saveAll(List<MainTask> mainTasks) {
		return mainTaskRepository.saveAll(mainTasks);
	}
}
