package com.sopt.todomate.domain.maintask.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.repository.MainTaskRepository;
import com.sopt.todomate.domain.maintask.exception.MainTaskNotFoundException;
import com.sopt.todomate.domain.user.domain.entity.User;
import com.sopt.todomate.domain.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainTaskGetService {
	private final MainTaskRepository mainTaskRepository;
	private final UserRepository userRepository;

	public MainTask findByMainTaskId(long mainTaskId) {
		return mainTaskRepository.findById(mainTaskId).orElseThrow(MainTaskNotFoundException::new);
	}

	public List<MainTask> findAllByTemplateId(long templateId) {
		return mainTaskRepository.findAllByTemplateTaskId(templateId);
	}

	public List<MainTask> findAllByTemplateIdAndAfterDate(long templateId, LocalDateTime date) {
		return mainTaskRepository.findAllByTemplateTaskIdAndTaskDateAfter(templateId, date);
	}

	public List<MainTask> findAllByUserIdAndTaskDate(Long userId, LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		return mainTaskRepository.findAllByUserIdAndTaskDateRangeOrderByCreatedAtDesc(userId, start, end);
	}

	public long findAmountByCategory(User user, CategoryType categoryType, LocalDateTime taskDate) {
		LocalDateTime start = taskDate.toLocalDate().atStartOfDay();
		LocalDateTime end = start.plusDays(1);

		return mainTaskRepository.countByCategoryAndUserAndTaskDateBetween(categoryType, user, start, end);
	}

	public List<MainTask> findALlByUserAndDate(User user, LocalDate localDate) {
		LocalDateTime start = localDate.atStartOfDay();
		LocalDateTime end = localDate.plusDays(1).atStartOfDay();

		return mainTaskRepository.findAllByTaskDateBetweenAndUser(start, end, user);
	}
}
