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
    boolean existsByIdAndSurveyId(Long id, Long surveyId);

    /**
     * Anything that modifies the table is done via transactions.
     * This means that you need to enable transactional with the tag, otherwise it will not work.
     * Since this is a custom, derived, repository method we have to tag it in order for it to work.
     *
     * @param id Id for the question to find.
     * @param surveyId Id for the survey the question is linked to.
     */
    @Transactional
    @Modifying
    void deleteByIdAndSurveyId(Long id, Long surveyId);
}
