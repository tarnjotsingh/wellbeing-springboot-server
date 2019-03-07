package com.reading.tvirdee.project.springbootdockermysql.repository;

import com.reading.tvirdee.project.springbootdockermysql.domain.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Choice domain.
 */
@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    Optional<Choice> findByIdAndQuestionId(Long id, Long questionId);
    List<Choice> findByQuestionId(Long questionId);
    boolean existsByIdAndQuestionId(Long id, Long questionId);

    @Transactional
    @Modifying
    void deleteByIdAndQuestionId(Long id, Long questionId);
}