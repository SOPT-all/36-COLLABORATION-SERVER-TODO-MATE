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

	@Column(name = "task_content")
	private String taskContent;

	@Column(name = "start_at")
	private LocalDateTime startAt;

	@Column(name = "end_at")
	private LocalDateTime endAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "routin_cycle")
	private RoutineType routineType;

	@Column(name = "priority")
	private Long priority;

	@Column(name = "category")
	private String category;

	@Column(name = "task_date")
	private LocalDateTime taskDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "completed")
	private Boolean completed;

	@Column(name = "template_task_id")
	private Long templateTaskId;

	@Builder
	private MainTask(String taskContent, LocalDateTime startAt, LocalDateTime endAt, RoutineType routineType,
		Long priority,
		String category, LocalDateTime taskDate, User user, Boolean completed, long templateTaskId) {
		this.taskContent = taskContent;
		this.startAt = startAt;
		this.endAt = endAt;
		this.routineType = routineType;
		this.priority = priority;
		this.category = category;
		this.taskDate = taskDate;
		this.user = user;
		this.completed = completed;
		this.templateTaskId = templateTaskId;
	}

	public static MainTask createRecurring(String content, User user, LocalDateTime taskDate,
		LocalDateTime startAt, LocalDateTime endAt,
		RoutineType routineType) {
		return builder()
			.taskContent(content)
			.taskDate(taskDate)
			.startAt(startAt)
			.endAt(endAt)
			.routineType(routineType)
			.user(user)
			.completed(false)
			.build();
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
}
