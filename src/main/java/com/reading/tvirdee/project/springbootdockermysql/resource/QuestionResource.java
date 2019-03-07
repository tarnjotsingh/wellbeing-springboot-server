package com.reading.tvirdee.project.springbootdockermysql.resource;

import com.codahale.metrics.annotation.Timed;
import com.reading.tvirdee.project.springbootdockermysql.domain.Question;
import com.reading.tvirdee.project.springbootdockermysql.repository.ChoiceRepository;
import com.reading.tvirdee.project.springbootdockermysql.repository.QuestionRepository;
import com.reading.tvirdee.project.springbootdockermysql.repository.SurveyRepository;
import com.reading.tvirdee.project.springbootdockermysql.resource.errors.BadRequestAlertException;
import com.reading.tvirdee.project.springbootdockermysql.resource.errors.ResourceNotFoundException;
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
 * REST controller for managing Question.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    //Can look into HATEOAS - hypertext as the engine of application state
    //https://www.baeldung.com/spring-hateoas-tutorial

    public QuestionResource(QuestionRepository questionRepository, SurveyRepository surveyRepository) {
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
    }


    /**
     * POST  /questions : Create a new question.
     *
     * @param question the question to create
     * @return the ResponseEntity with status 201 (Created) and with body the new question, or with status 400 (Bad Request) if the question has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/questions")
    @Timed
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) throws URISyntaxException {
        log.debug("REST request to save Question : {}", question);
        if (question.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Question result = questionRepository.save(question);
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /questions : Updates an existing question.
     *
     * @param question the questio//import com.codahale.metrics.annotation.Timed;n to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated question,
     * or with status 400 (Bad Request) if the question is not valid,
     * or with status 500 (Internal Server Error) if the question couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/questions")
    @Timed
    public ResponseEntity<Question> updateQuestion(@RequestBody Question question) throws URISyntaxException {
        log.debug("REST request to update Question : {}", question);
        if (question.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Question result = questionRepository.save(question);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, question.getId().toString()))
            .body(result);
    }

    /**
     * GET  /questions : get all the questions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of questions in body
     */
    @GetMapping("/questions")
    @Timed
    public List<Question> getAllQuestions() {
        log.debug("REST request to get all Questions");
        return questionRepository.findAll();
    }

    /**
     * GET /surveys/{surveyId}/questions : get all the questions for the given surveyId
     *
     * @param surveyId the survey to get the questions for
     * @return the List with status `
     */
    @GetMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<List<Question>> getAllQuestionsByServerId(@PathVariable (value = "surveyId") Long surveyId) {
        log.debug("REST request to get all Questions by Survey ID : {}", surveyId);

        List<Question> qList = questionRepository.findBySurveyId(surveyId);
        log.debug("Found questions for survey with Survey ID : {}", surveyId);

        return ResponseEntity.ok().body(qList);
    }

    /**
     * GET /surveys/{surveyId}/questions/{questionId}
     * @param surveyId
     * @param questionId
     * @return ResponseEntity of the returned Question object
     */
    @GetMapping("/surveys/{surveyId}/questions/{questionId}")
    public ResponseEntity<Question> getQuestionById(@PathVariable (value = "surveyId") Long surveyId,
                                                    @PathVariable (value = "questionId") Long questionId) {
        log.debug("REST request to get Question with ID : {} from Survey with ID : {}", questionId, surveyId);

        Question question = questionRepository.findByIdAndSurveyId(questionId, surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Question with Id: " + questionId + " in Survey with ID: " + surveyId + " not found."));

        return ResponseEntity.ok().body(question);
    }

    /**
     * POST /surveys/{surveyId}/questions
     *
     * @param surveyId target survey where the question to create is linked to
     * @param question body
     * @return Response entity with the newly created question
     * @throws URISyntaxException
     */
    @PostMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<Question> createQuestionForSurvey(@PathVariable (value = "surveyId") Long surveyId,
                                                            @RequestBody Question question) throws URISyntaxException {
        log.debug("REST request to save Question : {} in survey : {}", question, surveyId);
        if (question.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Attempt to fetch the desired survey to add the new question to.
        // Exception thrown on failure to find the requested survey.
        Question toReturn = surveyRepository.findById(surveyId).map(survey -> {
            question.setSurvey(survey);
            return questionRepository.save(question);
        }).orElseThrow( () -> new ResourceNotFoundException("Survey with ID " + surveyId + " not found."));

        return ResponseEntity.created(new URI("/api/surveys/" + surveyId + "/questions/" + toReturn.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, toReturn.getId().toString()))
                .body(toReturn);
    }

    /**
     * PUT /surveys/{surveyId}/questions/{questionId}
     * For updating a target question in a target survey.
     * ID for target question to update must be in the JSON body of the request.
     *
     * @param surveyId survey with the question to update
     * @param updatedQuestion JSON body with the new values for the target question
     * @return ResponseEntity with the body of the updated question.
     */
    @PutMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<Question> updatedQuestion(@PathVariable (value = "surveyId") Long surveyId,
                                                    @RequestBody Question updatedQuestion) throws URISyntaxException {
        // Check if the survey exists, if survey does not exist then the question does not.
        // At least not under the target survey.
        if(!surveyRepository.existsById(surveyId))
            throw new ResourceNotFoundException("Survey with ID " + surveyId + " not found.");

        // Ensure that there is an ID in the request body, otherwise PUT obviously will not work.
        if (updatedQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Attempt to find the question via ID and apply updated values from the request body.
        Question question = questionRepository.findById(updatedQuestion.getId()).map(q -> {
            q.setQuestion(updatedQuestion.getQuestion());
            return questionRepository.save(q);
        }).orElseThrow(() -> new ResourceNotFoundException(""));

        return ResponseEntity.created(new URI("/api/surveys/" + surveyId + "/questions/" + question.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, question.getId().toString()))
                .body(question);
    }

    /**
     * GET  /questions/:id : get the "id" question.
     *
     * @param id the id of the question to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the question, or with status 404 (Not Found)
     */
    @GetMapping("/questions/{id}")
    @Timed
    public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);

        //Optional allows a value to be null, this is because the requested Question, by id, may not be found.
        Optional<Question> question = questionRepository.findById(id);
        //Obviously handle the potential error
        return ResponseEntity.accepted().body(question.get());
        //return ResponseUtil.wrapOrNotFound(question);
    }

    /**
     * DELETE  /questions/:id : delete the "id" question.
     *
     * @param id the id of the question to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/surveys/{surveyId}/questions/{questionId}")
    @Timed
    public ResponseEntity<Void> deleteQuestion(@PathVariable (value = "surveyId") Long surveyId,
                                               @PathVariable (value = "questionId") Long id) {
        log.debug("REST request to delete Question : {} in Survey : {}", id, surveyId);

        //questionRepository.deleteByIdAndSurveyId(surveyId, id);
        questionRepository.deleteByIdAndSurveyId(id, surveyId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
