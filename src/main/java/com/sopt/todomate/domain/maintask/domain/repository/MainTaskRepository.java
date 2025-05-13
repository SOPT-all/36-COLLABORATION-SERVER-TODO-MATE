package com.sopt.todomate.domain.maintask.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.user.domain.entity.User;

@Repository
public interface MainTaskRepository extends JpaRepository<MainTask, Long> {
	List<MainTask> findAllByUserAndTaskContent(User user, String content);

	List<MainTask> findAllByTemplateTaskId(long templateId);
}
