package com.reading.tvirdee.project.springbootdockermysql.repository;

import com.reading.tvirdee.project.springbootdockermysql.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRespository extends JpaRepository<Survey, Long> {
}
