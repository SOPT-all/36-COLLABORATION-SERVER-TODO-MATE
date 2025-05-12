package com.sopt.todomate.domain.user.domain.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.sopt.todomate.domain.user.domain.entity.User;
import com.sopt.todomate.domain.user.domain.repository.UserRepository;
import com.sopt.todomate.domain.user.exception.UserNotFoundException;

@ActiveProfiles("test")
@SpringBootTest
class UserGetServiceTest {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserGetService userGetService;

	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

	@DisplayName("등록된 사용자를 id로 조회한다.")
	@Test
	void get() {
		// given
		User user = User.builder().userName("testUser").build();
		User savedUser = userRepository.save(user);
		Long userId = savedUser.getId();

		// when
		User findedUser = userGetService.findByUserId(userId);

		// then
		assertThat(findedUser.getId()).isEqualTo(userId);
		assertThat(findedUser.getUserName()).isEqualTo("testUser");
	}

	@DisplayName("등록되지 사용자를 조회할 경우 예외가 발생한다")
	@Test
	void throwIfUserNotFound() {
		//given
		User user = User.builder().userName("testUser").build();
		User savedUser = userRepository.save(user);
		long invalidUserId = savedUser.getId() + 1;

		//when & then
		assertThatThrownBy(() -> userGetService.findByUserId(invalidUserId))
			.isInstanceOf(UserNotFoundException.class)
			.hasMessage("사용자가 존재하지 않습니다.");
	}

}
