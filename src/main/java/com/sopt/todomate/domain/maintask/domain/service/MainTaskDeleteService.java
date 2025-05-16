package com.sopt.todomate.domain.maintask.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.repository.MainTaskRepository;
import com.sopt.todomate.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainTaskDeleteService {
	private final MainTaskRepository mainTaskRepository;

	public void delete(MainTask mainTask) {
		mainTaskRepository.delete(mainTask);
	}

	public void deleteAll(List<MainTask> mainTasks) {
		mainTaskRepository.deleteAll(mainTasks);
	}

	public void deleteAllByUser(User user) {
		mainTaskRepository.deleteAllByUser(user);
	}
}
