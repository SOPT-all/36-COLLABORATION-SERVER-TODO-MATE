package com.sopt.todomate.domain.maintask.application.usecase;

import static com.sopt.todomate.domain.maintask.domain.entity.Importance.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

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
import com.sopt.todomate.domain.maintask.exception.AccessDeniedException;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateRequest;
import com.sopt.todomate.domain.maintask.presentation.dto.MainTaskCreateResponse;
import com.sopt.todomate.domain.maintask.presentation.dto.SubTaskDto;
import com.sopt.todomate.domain.subtask.domain.entity.SubTask;
import com.sopt.todomate.domain.subtask.domain.repository.SubTaskRepository;
import com.sopt.todomate.domain.subtask.domain.service.SubTaskGetService;
import com.sopt.todomate.domain.subtask.exception.MaxSubTaskException;
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
			MEDIUM,        // priority
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
		assertEquals(MEDIUM, savedMainTask.getImportance());

		List<com.sopt.todomate.domain.subtask.domain.entity.SubTask> savedSubTasks =
			subTaskRepository.findAllByMainTask(savedMainTask);
		assertEquals(1, savedSubTasks.size());
		assertEquals("통합테스트 서브태스크", savedSubTasks.get(0).getContent());
	}

	@Test
	@DisplayName("유저는 한 태스크에 세개의 서브태스크만 생성할 수 있다.")
	void testMaxSubtasks() {
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
			MEDIUM,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false), new SubTaskDto("통합테스트 서브태스크2", false),
				new SubTaskDto("통합테스트 서브태스크3", false), new SubTaskDto("통합테스트 서브태스크4", false))
		);

		// when & then
		assertThatThrownBy(() -> mainTaskManageUsecase.execute(MainTaskCommand.from(request), savedUser.getId()))
			.isInstanceOf(MaxSubTaskException.class);
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
			MEDIUM,          // priority
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
		assertEquals(MEDIUM, response.importance());
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
			assertEquals(MEDIUM, task.getImportance());
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
			MEDIUM,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"변경하는 태스크",
			List.of(new SubTaskUpdateCommand("통합테스트 서브태스크", true)),
			false
		);

		//when

		mainTaskManageUsecase.update(response.mainTaskId(), mainTaskUpdateCommand, savedUser.getId());

		//then
		MainTask mainTask = mainTaskRepository.findById(response.mainTaskId()).get();
		assertThat(mainTask.getTaskContent()).isEqualTo("변경하는 태스크");
		assertThat(mainTask.getImportance()).isEqualTo(MEDIUM);
	}

	@DisplayName("사용자는 세개 초과의 서브태스크를 추가할 수 없다.")
	@Test
	void updateOverSubTaskMax() {
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
			MEDIUM,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"변경하는 태스크",
			List.of(new SubTaskUpdateCommand("통합테스트 서브태스크", true), new SubTaskUpdateCommand("통합테스트 서브태스크2", true),
				new SubTaskUpdateCommand("통합테스트 서브태스크3", true), new SubTaskUpdateCommand("통합테스트 서브태스크4", true)),
			false
		);

		//when & then
		assertThatThrownBy(
			() -> mainTaskManageUsecase.update(response.mainTaskId(), mainTaskUpdateCommand, savedUser.getId()))
			.isInstanceOf(MaxSubTaskException.class);

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
			MEDIUM,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false), new SubTaskDto("통합테스트 서브태스크2", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"통합테스트 태스크",
			List.of(new SubTaskUpdateCommand("변경된 서브태스크", true),
				new SubTaskUpdateCommand("변경된 서브태스크2", false)),
			false
		);

		//when

		mainTaskManageUsecase.update(response.mainTaskId(), mainTaskUpdateCommand, savedUser.getId());

		//then
		MainTask mainTask = mainTaskGetService.findByMainTaskId(response.mainTaskId());
		List<SubTask> subTasks = subTaskGetService.findAllByMainTask(mainTask);

		assertThat(subTasks).extracting("content", "completed")
			.containsAnyOf(
				tuple("변경된 서브태스크", true),
				tuple("변경된 서브태스크2", false)
			);

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
			MEDIUM,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false), new SubTaskDto("통합테스트 서브태스크2", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"변경된 태스크 제목",
			List.of(new SubTaskUpdateCommand("변경된 서브태스크", true),
				new SubTaskUpdateCommand("변경된 서브태스크2", false)),
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
			MEDIUM,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false), new SubTaskDto("통합테스트 서브태스크2", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"변경된 태스크 제목",
			List.of(new SubTaskUpdateCommand("변경된 서브태스크", true)),
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

	@DisplayName("사용자가 루틴에 새로운 서브 태스크를 추가할 수 있다.")
	@Test
	void updateNewSubTask() {
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
			MEDIUM,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"변경된 태스크 제목",
			List.of(new SubTaskUpdateCommand("변경된 서브태스크", true),
				new SubTaskUpdateCommand("새로 추가한 서브태스크", false)),
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

			assertThat(subTasks).extracting("content")
				.containsAnyOf(
					"변경된 서브태스크", "새로 추가한 서브태스크"
				);
		}

		MainTask mainTask = mainTaskGetService.findByMainTaskId(response.mainTaskId());
		List<SubTask> subTasks = subTaskGetService.findAllByMainTask(mainTask);

		assertThat(subTasks.get(0).getCompleted()).isTrue();
	}

	@DisplayName("사용자는 다른 사용자의 일정을 수정할 수 없다.")
	@Test
	void updateAnotherUsersTask() {
		//given

		LocalDateTime now = LocalDateTime.now();

		User testUser = User.builder()
			.userName("반복태스크테스트유저")
			.build();

		User testUser2 = User.builder()
			.userName("반복태스크테스트유저2")
			.build();

		User savedUser1 = userRepository.save(testUser);
		User savedUser2 = userRepository.save(testUser2);

		MainTaskCreateRequest request = new MainTaskCreateRequest(
			"통합테스트 태스크",
			null,       // startAt
			null,       // endAt
			RoutineType.NONE,
			MEDIUM,        // priority
			CategoryType.CATEGORY1,     // category
			now,        // taskDate
			false,      // completed
			List.of(new SubTaskDto("통합테스트 서브태스크", false))
		);

		MainTaskCreateResponse response = mainTaskManageUsecase.execute(MainTaskCommand.from(request),
			savedUser1.getId());

		MainTaskUpdateCommand mainTaskUpdateCommand = new MainTaskUpdateCommand(
			"변경된 태스크 제목",
			List.of(new SubTaskUpdateCommand("변경된 서브태스크", true),
				new SubTaskUpdateCommand("새로 추가한 서브태스크", false)),
			true
		);

		//when & then
		assertThatThrownBy(
			() -> mainTaskManageUsecase.update(response.mainTaskId(), mainTaskUpdateCommand, savedUser2.getId()))
			.isInstanceOf(AccessDeniedException.class);

	}

}
