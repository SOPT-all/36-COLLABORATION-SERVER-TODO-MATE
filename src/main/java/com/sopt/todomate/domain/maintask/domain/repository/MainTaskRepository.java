package com.sopt.todomate.domain.maintask.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopt.todomate.domain.maintask.domain.entity.MainTask;

@Repository
public interface MainTaskRepository extends JpaRepository<MainTask, Long> {
}
