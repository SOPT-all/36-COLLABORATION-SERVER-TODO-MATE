package com.sopt.todomate.domain.maintask.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.repository.MainTaskRepository;
import com.sopt.todomate.domain.maintask.exception.MainTaskNotFoundException;
import com.sopt.todomate.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainTaskGetService {
	private final MainTaskRepository mainTaskRepository;

	public MainTask findByMainTaskId(long mainTaskId) {
		return mainTaskRepository.findById(mainTaskId).orElseThrow(MainTaskNotFoundException::new);
	}

	public List<MainTask> findAllByTemplateId(long templateId) {
		return mainTaskRepository.findAllByTemplateTaskId(templateId);
	}

	public List<MainTask> findAllByTemplateIdAndAfterDate(long templateId, LocalDateTime date) {
		return mainTaskRepository.findAllByTemplateTaskIdAndTaskDateAfter(templateId, date);
	}

	public List<MainTask> findAllByUserIdAndDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
		return mainTaskRepository.findAllByUserIdAndDateRange(userId, start, end);
	}

	public long findAmountByCategory(User user, CategoryType categoryType) {
		System.out.println(mainTaskRepository.countByCategoryAndUser(categoryType, user));
		return mainTaskRepository.countByCategoryAndUser(categoryType, user);
	}
}
