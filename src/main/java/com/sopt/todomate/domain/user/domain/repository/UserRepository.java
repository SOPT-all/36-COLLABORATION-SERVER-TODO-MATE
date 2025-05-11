package com.sopt.todomate.domain.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopt.todomate.domain.user.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
