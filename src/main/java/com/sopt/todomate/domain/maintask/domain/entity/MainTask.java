package com.sopt.todomate.domain.maintask.domain.entity;

import java.time.LocalDateTime;

import com.sopt.todomate.domain.user.domain.entity.User;
import com.sopt.todomate.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "main_tasks")
public class MainTask extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "main_task_id")
	private Long id;

	@Column(name = "task_content", nullable = false)
	private String taskContent;

	@Column(name = "start_at")
	private LocalDateTime startAt;

	@Column(name = "end_at")
	private LocalDateTime endAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "routin_type", nullable = false)
	private RoutineType routineType;

	@Enumerated(EnumType.STRING)
	@Column(name = "importance", nullable = false)
	private Importance importance;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private CategoryType category;

	@Column(name = "task_date", nullable = false)
	private LocalDateTime taskDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "completed", nullable = false)
	private Boolean completed;

	@Column(name = "template_task_id", nullable = false)
	private Long templateTaskId;

	@Builder
	private MainTask(String taskContent, LocalDateTime startAt, LocalDateTime endAt, RoutineType routineType,
		Importance importance,
		CategoryType category, LocalDateTime taskDate, User user, Boolean completed, long templateTaskId) {
		this.taskContent = taskContent;
		this.startAt = startAt;
		this.endAt = endAt;
		this.routineType = routineType;
		this.importance = importance;
		this.category = category;
		this.taskDate = taskDate;
		this.user = user;
		this.completed = completed;
		this.templateTaskId = templateTaskId;
	}

	public void addAuthor(User user) {
		this.user = user;
	}

	public boolean isCompleted() {
		return this.completed;
	}

	public void updateTemplateTask(long id) {
		this.templateTaskId = id;
	}

	public void updateContent(String content) {
		this.taskContent = content;
	}

	public void updateImportance(Importance importance) {
		this.importance = importance;
	}
}
