package com.sopt.todomate.domain.subtask.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
	List<SubTask> findAllByMainTask(MainTask mainTask);

	void deleteAllByMainTask(MainTask mainTask);
}
