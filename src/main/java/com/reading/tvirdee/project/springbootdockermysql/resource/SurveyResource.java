package com.reading.tvirdee.project.springbootdockermysql.resource;

import com.codahale.metrics.annotation.Timed;
import com.reading.tvirdee.project.springbootdockermysql.domain.Question;
import com.reading.tvirdee.project.springbootdockermysql.domain.Survey;
import com.reading.tvirdee.project.springbootdockermysql.repository.SurveyRepository;
import com.reading.tvirdee.project.springbootdockermysql.resource.errors.BadRequestAlertException;
import com.reading.tvirdee.project.springbootdockermysql.resource.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Survey.
 */
@RestController
@RequestMapping("/api")
public class SurveyResource {

    private final Logger log = LoggerFactory.getLogger(SurveyResource.class);

    private static final String ENTITY_NAME = "survey";

    private final SurveyRepository surveyRepository;

    public SurveyResource(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    /**
     * POST  /surveys : Create a new survey.
     *
     * @param survey the survey to create
     * @return the ResponseEntity with status 201 (Created) and with body the new survey, or with status 400 (Bad Request) if the survey has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/surveys")
    @Timed
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) throws URISyntaxException {
        log.debug("REST request to save Survey : {}", survey);
        if (survey.getId() != null) {
            throw new BadRequestAlertException("A new survey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Survey result = surveyRepository.save(survey);
        return ResponseEntity.created(new URI("/api/surveys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /surveys : Updates an existing survey.
     *
     * @param survey the survey to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated survey,
     * or with status 400 (Bad Request) if the survey is not valid,
     * or with status 500 (Internal Server Error) if the survey couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/surveys")
    @Timed
    public ResponseEntity<Survey> updateSurvey(@RequestBody Survey survey) throws URISyntaxException {
        log.debug("REST request to update Survey : {}", survey);
        if (survey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Survey result = surveyRepository.save(survey);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, survey.getId().toString()))
            .body(result);
    }

    /**
     * GET  /surveys : get all the surveys.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of surveys in body
     */
    @GetMapping("/surveys")
    @Timed
    public List<Survey> getAllSurveys() {
        log.debug("REST request to get all Surveys");
        return surveyRepository.findAll();
    }

    /**
     * GET  /surveys/:id : get the "id" survey.
     *
     * @param id the id of the survey to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the survey, or with status 404 (Not Found)
     */
    @GetMapping("/surveys/{id}")
    @Timed
    public ResponseEntity<Survey> getSurvey(@PathVariable Long id) {
        log.debug("REST request to get Survey : {}", id);
        Optional<Survey> survey = surveyRepository.findById(id);
        if(survey.isPresent()){
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityGetAlert(ENTITY_NAME,id.toString()))
                    .body(survey.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * DELETE  /surveys/:id : delete the "id" survey.
     *
     * @param id the id of the survey to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/surveys/{id}")
    @Timed
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        log.debug("REST request to delete Survey : {}", id);
        surveyRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
