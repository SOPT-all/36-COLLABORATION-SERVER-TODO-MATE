package com.sopt.todomate.domain.maintask.application.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.sopt.todomate.domain.maintask.application.dto.MainTaskCommand;
import com.sopt.todomate.domain.maintask.application.dto.MainTaskUpdateCommand;
import com.sopt.todomate.domain.maintask.application.dto.SubTaskUpdateCommand;
import com.sopt.todomate.domain.maintask.domain.entity.CategoryType;
import com.sopt.todomate.domain.maintask.domain.entity.MainTask;
import com.sopt.todomate.domain.maintask.domain.entity.RoutineType;
import com.sopt.todomate.domain.maintask.domain.repository.MainTaskRepository;
import com.sopt.todomate.domain.maintask.domain.service.MainTaskGetService;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateRequest;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateResponse;
import com.sopt.todomate.domain.maintask.presentation.dto.SubTaskDto;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.repository.SubTaskRepository;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskGetService;
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
	private SubTaskRepository subTaskRepository;
	@Autowired
	private SubTaskGetService subTaskGetService;
	@Autowired
	private MainTaskGetService mainTaskGetService;

	@Test
	@DisplayName("단일 태스크 생성 통합 테스트")
	void testCreateSingleTask() {
		// Given - 테스트 사용자 생성 및 저장
		User testUser = User.builder()
			.userName("통합테스트유저")
			.build();
		User savedUser = userRepository.save(testUser);

		LocalDateTime now = LocalDateTime.now();

		// 테스트 요청 생성
		MainTaskCreateRequest request = new MainTaskCreateRequest(
			"통합테스트 태스크",
			null,       // startAt
			null,       // endAt
			RoutineType.NONE,       // routinCycle
			1,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false))
		);

		// When - usecase 실행
		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		// Then - 응답 검증
		assertNotNull(response);
		assertEquals("통합테스트 태스크", response.taskContent());
		assertEquals(1, response.subTasks().size());
		assertEquals("통합테스트 서브태스크", response.subTasks().get(0).content());

		MainTask savedMainTask = mainTaskRepository.findById(response.mainTaskId()).orElse(null);
		assertNotNull(savedMainTask);
		assertEquals("통합테스트 태스크", savedMainTask.getTaskContent());
		assertEquals(1L, savedMainTask.getPriority());

		List<com.sopt.todomate.domain.subtask.domain.entity.SubTask> savedSubTasks =
			subTaskRepository.findAllByMainTask(savedMainTask);
		assertEquals(1, savedSubTasks.size());
		assertEquals("통합테스트 서브태스크", savedSubTasks.get(0).getContent());
	}

	@Test
	@DisplayName("반복 태스크 생성 통합 테스트")
	void testCreateRecurringTask() {
		// Given
		User testUser = User.builder()
			.userName("반복태스크테스트유저")
			.build();
		User savedUser = userRepository.save(testUser);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = now.plusDays(2);

		MainTaskCreateRequest request = new MainTaskCreateRequest(
			"반복 태스크",
			now,        // startAt
			endDate,    // endAt
			RoutineType.DAILY,    // routinCycle
			2,          // priority
			CategoryType.CATEGORY1,     // category
			now,       // taskDate
			false,      // completed
			List.of(new SubTaskDto("반복 서브태스크", false))
		);

		// When
		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		// Then
		assertNotNull(response);
		assertEquals("반복 태스크", response.taskContent());
		assertEquals(2, response.priority());
		assertEquals(RoutineType.DAILY, response.routineType());

		// 응답에 포함된 서브태스크 확인 (대표 태스크의 서브태스크)
		assertEquals(1, response.subTasks().size());
		assertEquals("반복 서브태스크", response.subTasks().get(0).content());
		assertFalse(response.subTasks().get(0).completed());

		// 저장된 모든 메인 태스크 조회
		List<MainTask> savedMainTasks = mainTaskRepository.findAllByUserAndTaskContent(
			savedUser, "반복 태스크");

		// 3일치의 태스크가 생성되었는지 확인
		assertEquals(3, savedMainTasks.size());

		// 날짜별 태스크 확인
		boolean foundToday = false;
		boolean foundTomorrow = false;
		boolean foundDayAfterTomorrow = false;

		int totalSubTasksCount = 0;

		for (MainTask task : savedMainTasks) {
			assertEquals("반복 태스크", task.getTaskContent());
			assertEquals(2L, task.getPriority());
			assertEquals(CategoryType.CATEGORY1, task.getCategory());
			assertEquals(RoutineType.DAILY, task.getRoutineType());
			assertEquals(now, task.getStartAt());
			assertEquals(endDate, task.getEndAt());
			assertFalse(task.isCompleted());

			LocalDateTime taskDate = task.getTaskDate();
			if (isSameDay(taskDate, now)) {
				foundToday = true;
			} else if (isSameDay(taskDate, now.plusDays(1))) {
				foundTomorrow = true;
			} else if (isSameDay(taskDate, now.plusDays(2))) {
				foundDayAfterTomorrow = true;
			}

			// 각 태스크의 서브태스크 조회
			List<com.sopt.todomate.domain.subtask.domain.entity.SubTask> subTasks =
				subTaskRepository.findAllByMainTask(task);

			// 각 태스크마다 서브태스크 1개가 있어야 함
			assertEquals(1, subTasks.size());

			// 서브태스크 내용 확인
			com.sopt.todomate.domain.subtask.domain.entity.SubTask subTask = subTasks.get(0);
			assertEquals("반복 서브태스크", subTask.getContent());
			assertFalse(subTask.isCompleted());
			assertEquals(task.getId(), subTask.getMainTask().getId());

			totalSubTasksCount += subTasks.size();
		}

		// 날짜별 태스크가 모두 생성되었는지 확인
		assertTrue(foundToday, "오늘 날짜의 태스크가 없습니다");
		assertTrue(foundTomorrow, "내일 날짜의 태스크가 없습니다");
		assertTrue(foundDayAfterTomorrow, "모레 날짜의 태스크가 없습니다");

		// 총 서브태스크 개수 확인 (3개의 태스크 * 1개의 서브태스크 = 3개)
		assertEquals(3, totalSubTasksCount);

		// DB에 저장된 총 서브태스크 수 확인 (다른 방법으로도 검증)
		long totalSubTasksInDb = subTaskRepository.count();
		assertEquals(3, totalSubTasksInDb);
	}

	// 날짜 비교를 위한 헬퍼 메서드 (시간은 무시하고 날짜만 비교)
	private boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
		return date1.getYear() == date2.getYear() &&
			date1.getMonth() == date2.getMonth() &&
			date1.getDayOfMonth() == date2.getDayOfMonth();
	}

	@DisplayName("사용자는 메인태스크의 내용을 수정할 수 있다.")
	@Test
	void updateMainTaskContents() {
		//given
		User testUser = User.builder()
			.userName("반복태스크테스트유저")
			.build();
		User savedUser = userRepository.save(testUser);

		LocalDateTime now = LocalDateTime.now();

		MainTaskCreateRequest request = new MainTaskCreateRequest(
			"통합테스트 태스크",
			null,       // startAt
			null,       // endAt
			RoutineType.NONE,       // routinCycle
			1,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"변경하는 태스크",
			List.of(new SubTaskUpdateCommand(response.subTasks().get(0).subTaskId(), "통합테스트 서브태스크", true)),
			false
		);

		//when

		mainTaskManageUsecase.update(response.mainTaskId(), mainTaskUpdateCommand, savedUser.getId());

		//then
		MainTask mainTask = mainTaskRepository.findById(response.mainTaskId()).get();
		assertThat(mainTask.getTaskContent()).isEqualTo("변경하는 태스크");
	}

	@DisplayName("사용자는 서브태스크의 내용을 수정할 수 있다.")
	@Test
	void updateSubTasks() {
		//given
		User testUser = User.builder()
			.userName("반복태스크테스트유저")
			.build();
		User savedUser = userRepository.save(testUser);

		LocalDateTime now = LocalDateTime.now();

		MainTaskCreateRequest request = new MainTaskCreateRequest(
			"통합테스트 태스크",
			null,       // startAt
			null,       // endAt
			RoutineType.NONE,       // routinCycle
			1,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false), new SubTaskDto("통합테스트 서브태스크2", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"통합테스트 태스크",
			List.of(new SubTaskUpdateCommand(response.subTasks().get(0).subTaskId(), "변경된 서브태스크", true),
				new SubTaskUpdateCommand(response.subTasks().get(1).subTaskId(), "변경된 서브태스크2", false)),
			false
		);

		//when

		mainTaskManageUsecase.update(response.mainTaskId(), mainTaskUpdateCommand, savedUser.getId());

		//then

		Map<Long, String> expectedSubTaskContents = mainTaskUpdateCommand.subTasks().stream()
			.collect(Collectors.toMap(
				SubTaskUpdateCommand::id,
				SubTaskUpdateCommand::content
			));

		for (Long subTaskId : expectedSubTaskContents.keySet()) {
			SubTask savedSubTask = subTaskRepository.findById(subTaskId).orElse(null);

			String expectedContent = expectedSubTaskContents.get(subTaskId);
			assertThat(savedSubTask.getContent())
				.as("서브태스크 ID %d의 내용이 업데이트되어야 합니다", subTaskId)
				.isEqualTo(expectedContent);
		}
	}

	@DisplayName("사용자는 루틴의 모든 태스크를 변경할 수 있다.")
	@Test
	void updateAllRoutineContents() {
		//given

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = now.plusDays(2);

		User testUser = User.builder()
			.userName("반복태스크테스트유저")
			.build();
		User savedUser = userRepository.save(testUser);

		MainTaskCreateRequest request = new MainTaskCreateRequest(
			"통합테스트 태스크",
			now,       // startAt
			endDate,       // endAt
			RoutineType.DAILY,
			1,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false), new SubTaskDto("통합테스트 서브태스크2", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"변경된 태스크 제목",
			List.of(new SubTaskUpdateCommand(response.subTasks().get(0).subTaskId(), "변경된 서브태스크", true),
				new SubTaskUpdateCommand(response.subTasks().get(1).subTaskId(), "변경된 서브태스크2", false)),
			true
		);

		//when
		mainTaskManageUsecase.update(response.mainTaskId(), mainTaskUpdateCommand, savedUser.getId());

		//then

		Long templateId = mainTaskGetService.findByMainTaskId(response.mainTaskId()).getTemplateTaskId();

		List<MainTask> allRoutineTasks = mainTaskGetService.findAllByTemplateId(templateId);

		for (MainTask task : allRoutineTasks) {
			assertThat(task.getTaskContent())
				.as("메인 태스크 ID %d의 내용이 변경되어야 합니다", task.getId())
				.isEqualTo("변경된 태스크 제목");

			List<SubTask> subTasks = subTaskGetService.findAllByMainTask(task);

			assertThat(subTasks).hasSize(2)
				.as("메인 태스크 ID %d는 2개의 서브태스크를 가져야 합니다", task.getId());

			assertThat(subTasks.get(0).getContent())
				.as("메인 태스크 ID %d의 첫 번째 서브태스크 내용이 변경되어야 합니다", task.getId())
				.isEqualTo("변경된 서브태스크");

			assertThat(subTasks.get(1).getContent())
				.as("메인 태스크 ID %d의 두 번째 서브태스크 내용이 변경되어야 합니다", task.getId())
				.isEqualTo("변경된 서브태스크2");
		}
	}

	@DisplayName("사용자느 루틴에 모든 태스크의 개수를 한번에 줄일 수 있다")
	@Test
	void updateAllRoutineContentsIfDelete() {
		//given

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = now.plusDays(2);

		User testUser = User.builder()
			.userName("반복태스크테스트유저")
			.build();
		User savedUser = userRepository.save(testUser);

		MainTaskCreateRequest request = new MainTaskCreateRequest(
			"통합테스트 태스크",
			now,       // startAt
			endDate,       // endAt
			RoutineType.DAILY,
			1,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false), new SubTaskDto("통합테스트 서브태스크2", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"변경된 태스크 제목",
			List.of(new SubTaskUpdateCommand(response.subTasks().get(0).subTaskId(), "변경된 서브태스크", true)),
			true
		);

		//when
		mainTaskManageUsecase.update(response.mainTaskId(), mainTaskUpdateCommand, savedUser.getId());

		//then

		Long templateId = mainTaskGetService.findByMainTaskId(response.mainTaskId()).getTemplateTaskId();

		List<MainTask> allRoutineTasks = mainTaskGetService.findAllByTemplateId(templateId);

		for (MainTask task : allRoutineTasks) {
			assertThat(task.getTaskContent())
				.as("메인 태스크 ID %d의 내용이 변경되어야 합니다", task.getId())
				.isEqualTo("변경된 태스크 제목");

			List<SubTask> subTasks = subTaskGetService.findAllByMainTask(task);

			assertThat(subTasks).hasSize(1)
				.as("메인 태스크 ID %d는 1개의 서브태스크를 가져야 합니다", task.getId());

			assertThat(subTasks.get(0).getContent())
				.as("메인 태스크 ID %d의 첫 번째 서브태스크 내용이 변경되어야 합니다", task.getId())
				.isEqualTo("변경된 서브태스크");
		}

		MainTask mainTask = mainTaskGetService.findByMainTaskId(response.mainTaskId());
		List<SubTask> subTasks = subTaskGetService.findAllByMainTask(mainTask);

		assertThat(subTasks.get(0).getCompleted()).isTrue();
	}

}
