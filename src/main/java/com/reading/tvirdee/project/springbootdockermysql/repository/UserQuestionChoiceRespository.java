package com.reading.tvirdee.project.springbootdockermysql.repository;

import com.reading.tvirdee.project.springbootdockermysql.domain.UserQuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionChoiceRespository extends JpaRepository<UserQuestionChoice, Long> {
}
