package com.sopt.todomate.domain.maintask.application.usecase;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.sopt.todomate.domain.maintask.application.dto.MainTaskCommand;
import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.repository.MainTaskRepository;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskGetService;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateRequest;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateResponse;
import com.sopt.todomate.domain.user.domain.entity.User;
import com.sopt.todomate.domain.user.domain.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MainTaskManageUsecaseTest {

	@Autowired
	private MainTaskManageUsecase mainTaskManageUsecase;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MainTaskRepository mainTaskRepository;

	@Autowired
	private MainTaskGetService mainTaskGetService;

	@Test
	@DisplayName("유저는 메인태스크를 생성할 수 있다.")
	void testCreateSingleTask() {
		// Given - 테스트 사용자 생성 및 저장
		User testUser = User.builder()
			.userName("통합테스트유저")
			.build();
		User savedUser = userRepository.save(testUser);

		LocalDateTime now = LocalDateTime.now();

		MainTaskCreateRequest request = new MainTaskCreateRequest(
			"통합테스트 태스크",
			CategoryType.CATEGORY1,
			now);

		// When - usecase 실행
		MainTaskCreateResponse response = mainTaskManageUsecase.createMainTask(MainTaskCommand.from(request),
			savedUser.getId());

		// Then - 응답 검증
		MainTask mainTask = mainTaskGetService.findByMainTaskId(response.mainTaskId());
		assertThat(mainTask.getTaskContent())
			.isEqualTo("통합테스트 태스크");
		assertThat(mainTask.getCategory())
			.isEqualTo(CategoryType.CATEGORY1);
		assertThat(mainTask.getTaskDate())
			.isEqualTo(now);
	}

}
