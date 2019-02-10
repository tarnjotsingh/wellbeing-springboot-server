package com.reading.tvirdee.project.springbootdockermysql.repository;

import com.reading.tvirdee.project.springbootdockermysql.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Question domain.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    //Page<Question> findBySurveyId(Long surveyId, Pageable pageable);
    List<Question> findBySurveyId(Long surveyId);
    Optional<Question> findByIdAndSurveyId(Long id, Long surveyId);

    @Transactional
    @Modifying
    void deleteByIdAndSurveyId(Long id, Long surveyId);
}
