package com.sopt.todomate.domain.subtask.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.sopt.todomate.domain.maintask.application.usecase.MainTaskManageUsecase;
import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.repository.MainTaskRepository;
import com.sopt.todomate.domain.subtask.application.dto.SubTaskCreateCommand;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskGetService;
import com.sopt.todomate.domain.subtask.presentation.dto.SubTaskCreateResponse;
import com.sopt.todomate.domain.user.domain.entity.User;
import com.sopt.todomate.domain.user.domain.repository.UserRepository;

import jakarta.transaction.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class SubTaskManageUsecaseTest {

	@Autowired
	private MainTaskManageUsecase mainTaskManageUsecase;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MainTaskRepository mainTaskRepository;

	@Autowired
	private SubTaskGetService subTaskGetService;

	@DisplayName("사용자는 서브태스크를 생성 할 수 있다.")
	@Test
	void createSubTask() {
		//given
		// Given - 테스트 사용자 생성 및 저장
		User testUser = User.builder()
			.userName("통합테스트유저")
			.build();
		User savedUser = userRepository.save(testUser);

		LocalDateTime now = LocalDateTime.now();

		MainTask mainTask = mainTaskRepository.save(
			MainTask.createMainTaskWithoutRoutine("content", CategoryType.CATEGORY1, now, savedUser));

		//when

		SubTaskCreateResponse response = mainTaskManageUsecase.createSubTask(savedUser.getId(), mainTask.getId(),
			new SubTaskCreateCommand("테스트 컨텐트"));

		//then

		SubTask subTask = subTaskGetService.findSubTaskById(response.id());

		assertThat(subTask.getContent()).isEqualTo("테스트 컨텐트");
		assertThat(subTask.getCompleted()).isEqualTo(false);
	}
}