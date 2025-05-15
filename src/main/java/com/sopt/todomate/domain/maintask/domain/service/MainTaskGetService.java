package com.sopt.todomate.domain.maintask.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.repository.MainTaskRepository;
import com.sopt.todomate.domain.maintask.exception.MainTaskNotFoundException;

import lombok.RequiredArgsConstructor;

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

	public List<MainTask> findAllByUserIdAndTaskDate(Long userId, LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		return mainTaskRepository.findAllByUserIdAndTaskDateRangeOrderByCreatedAtDesc(userId, start, end);
	}
}
