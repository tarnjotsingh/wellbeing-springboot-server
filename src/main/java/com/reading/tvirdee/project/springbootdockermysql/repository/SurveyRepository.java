package com.reading.tvirdee.project.springbootdockermysql.repository;

import com.reading.tvirdee.project.springbootdockermysql.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
