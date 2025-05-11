package com.sopt.todomate.domain.maintask.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.user.domain.entity.User;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "main_tasks")
public class MainTask {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "main_task_id")
	private Long mainTaskId;

	@Column(name = "task_content")
	private String taskContent;

	@Column(name = "start_at")
	private LocalDateTime startAt;

	@Column(name = "end_at")
	private LocalDateTime endAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "routin_cycle")
	private RoutineCycle routinCycle;

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

	@OneToMany(mappedBy = "mainTask", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubTask> subTasks = new ArrayList<>();

	@Builder
	private MainTask(String taskContent, LocalDateTime startAt, LocalDateTime endAt, RoutineCycle routineCycle,
		Long priority,
		String category, LocalDateTime taskDate, User user, Boolean completed, List<SubTask> subTasks) {
		this.taskContent = taskContent;
		this.startAt = startAt;
		this.endAt = endAt;
		this.routinCycle = routineCycle;
		this.priority = priority;
		this.category = category;
		this.taskDate = taskDate;
		this.user = user;
		this.completed = completed;
		this.subTasks = subTasks;
	}

	public void addSubTask(SubTask subTask) {
		subTasks.add(subTask);
		subTask.addMainTask(this);
	}
}
