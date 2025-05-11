package com.sopt.todomate.domain.user.domain.service;

import org.springframework.stereotype.Service;

import com.sopt.todomate.domain.user.domain.entity.User;
import com.sopt.todomate.domain.user.domain.repository.UserRepository;
import com.sopt.todomate.domain.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserGetService {
	private final UserRepository userRepository;

	public User findByUserId(long userId) {
		return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}
}
