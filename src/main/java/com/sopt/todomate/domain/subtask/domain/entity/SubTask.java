package com.sopt.todomate.domain.subtask.domain.entity;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sub_tasks")
public class SubTask extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sub_task_id")
	private Long id;

	@Column(name = "content")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "main_task_id")
	private MainTask mainTask;

	@Column(name = "completed", nullable = false)
	private Boolean completed;

	@Builder
	public SubTask(String content, MainTask mainTask, Boolean completed) {
		this.content = content;
		this.mainTask = mainTask;
		this.completed = completed;
	}

	public static SubTask create(String content, MainTask mainTask, Boolean completed) {
		return SubTask.builder()
			.content(content)
			.mainTask(mainTask)
			.completed(completed)
			.build();
	}

	public void addMainTask(MainTask mainTask) {
		this.mainTask = mainTask;
	}

	public boolean isCompleted() {
		return this.completed;
	}

}
