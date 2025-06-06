package com.sopt.todomate.domain.maintask.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.user.domain.entity.User;

@Repository
public interface MainTaskRepository extends JpaRepository<MainTask, Long> {
	List<MainTask> findAllByUserAndTaskContent(User user, String content);

	List<MainTask> findAllByTemplateTaskId(long templateId);

	@Query("SELECT m FROM MainTask m WHERE m.templateTaskId = :templateId AND m.taskDate > :date")
	List<MainTask> findAllByTemplateTaskIdAndTaskDateAfter(
		@Param("templateId") long templateId,
		@Param("date") LocalDateTime date);

	@Query("""
		    SELECT m FROM MainTask m
		    WHERE m.user.id = :userId
		    AND m.taskDate >= :start AND m.taskDate < :end
		    ORDER BY m.createdAt DESC
		""")
	List<MainTask> findAllByUserIdAndTaskDateRangeOrderByCreatedAtDesc(
		@Param("userId") Long userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	int countByCategoryAndUserAndTaskDateBetween(CategoryType categoryType, User user, LocalDateTime startDate,
		LocalDateTime endDate);

	List<MainTask> findAllByTaskDateBetweenAndUser(LocalDateTime start, LocalDateTime end, User user);

	void deleteAllByUser(User user);

	List<MainTask> findAllByUser(User user);
}
