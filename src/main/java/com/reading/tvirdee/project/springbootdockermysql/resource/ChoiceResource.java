package com.reading.tvirdee.project.springbootdockermysql.resource;

import com.codahale.metrics.annotation.Timed;
import com.reading.tvirdee.project.springbootdockermysql.domain.Choice;
import com.reading.tvirdee.project.springbootdockermysql.repository.ChoiceRepository;
import com.reading.tvirdee.project.springbootdockermysql.repository.QuestionRepository;
import com.reading.tvirdee.project.springbootdockermysql.repository.SurveyRepository;
import com.reading.tvirdee.project.springbootdockermysql.resource.errors.BadRequestAlertException;
import com.reading.tvirdee.project.springbootdockermysql.resource.errors.ResourceNotFoundException;
import com.reading.tvirdee.project.springbootdockermysql.resource.errors.DeletetionFailException;
import com.reading.tvirdee.project.springbootdockermysql.resource.util.HeaderUtil;

import com.reading.tvirdee.project.springbootdockermysql.resource.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing Choice.
 */
@RestController
@RequestMapping("/api")
public class ChoiceResource {

    private final Logger log = LoggerFactory.getLogger(ChoiceResource.class);

    private static final String ENTITY_NAME = "choice";

    private final ChoiceRepository choiceRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    public ChoiceResource(ChoiceRepository choiceRepository,
                          QuestionRepository questionRepository,
                          SurveyRepository surveyRepository) {
        this.choiceRepository = choiceRepository;
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
    }

    /**
     * POST  /choices : Create a new choice.
     *
     * @param choice the choice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new choice, or with status 400 (Bad Request) if the choice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/choices")
    @Timed
    public ResponseEntity<Choice> createChoice(@RequestBody Choice choice) throws URISyntaxException {
        log.debug("REST request to save Choice : {}", choice);
        if (choice.getId() != null) {
            throw new BadRequestAlertException("A new choice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Choice result = choiceRepository.save(choice);
        return ResponseEntity.created(new URI("/api/choices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * POST /surveys/{surveyId}/questions/{questionId}/choices :
     * Creates a new choice for the specified surveyQuestion
     *
     * @param surveyId Survey that the question is linked to
     * @param questionId Question to add the choice to
     * @param newChoice JSON body for the new choice to add to the question
     * @return ResponseEntity with JSON body of the new choice added to the question.
     */
    @PostMapping("/surveys/{surveyId}/questions/{questionId}/choices")
    @Timed
    public ResponseEntity<Choice> createChoiceForSurveyQuestion(@PathVariable(value = "surveyId") Long surveyId,
                                                                @PathVariable(value = "questionId") Long questionId,
                                                                @RequestBody Choice newChoice) throws URISyntaxException {
        log.debug("REST request to save Choice : {}", newChoice);
        log.debug("Saving choice to questionId {} in surveyId {}", questionId, surveyId);
        // Need to find the question by Id to see if the question exists then assign the question to it by querying questionRepo
        Choice toReturn = questionRepository.findByIdAndSurveyId(questionId, surveyId)
                .map(question -> {
                    newChoice.setQuestion(question);
                    return choiceRepository.save(newChoice);
                }).orElseThrow( () -> new ResourceNotFoundException(
                        "Question with ID " + questionId + " under Survey with ID " + surveyId + " not found."));

        return ResponseEntity.created(new URI("/api/surveys/" + surveyId + "/questions/" + questionId + "/choices" + toReturn.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, toReturn.getId().toString()))
                .body(toReturn);
    }

    /**
     * PUT  /choices : Updates an existing choice.
     *
     * @param choice the choice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated choice,
     * or with status 400 (Bad Request) if the choice is not valid,
     * or with status 500 (Internal Server Error) if the choice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/choices")
    @Timed
    public ResponseEntity<Choice> updateChoice(@RequestBody Choice choice) throws URISyntaxException {
        log.debug("REST request to update Choice : {}", choice);
        if (choice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Choice result = choiceRepository.save(choice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, choice.getId().toString()))
            .body(result);
    }

    /**
     * PUT /surveys/{surveyId}/questions/{questionId}/choices :
     * Update a choice for a given surveyQuestion
     *
     * @param surveyId Survey that the question is linked to
     * @param questionId Question that the choice to update is linked to
     * @param updatedChoice JSON body specifying the choice to update with it's new property values
     * @return JSON body of the updated surveyQuestion choice.
     */
    @PutMapping("/surveys/{surveyId}/questions/{questionId}/choices")
    @Timed
    public ResponseEntity<Choice> updateChoiceForSurveyQuestion(@PathVariable(value = "surveyId") Long surveyId,
                                                                @PathVariable(value = "questionId") Long questionId,
                                                                @RequestBody Choice updatedChoice) throws URISyntaxException {
        log.debug("PUT request to update choice for question with ID {} in survey with ID {}", questionId, surveyId);

        //Check if the question/survey exists before continuing, no point if it doesn't exist
        if(!questionRepository.existsByIdAndSurveyId(questionId, surveyId))
            throw new ResourceNotFoundException("Question with ID " + questionId + " Survey ID " + surveyId + " not found.");

        // Ensure that there is an ID in the request body, otherwise PUT obviously will not work since the resource doesn't exist.
        if (updatedChoice.getId() == null)
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");

        Choice toReturn = choiceRepository.findByIdAndQuestionId(updatedChoice.getId(), questionId)
                .map(c -> {
                    //Update the choice and weight then return the updated choice
                    c.setChoice(updatedChoice.getChoice());
                    c.setWeight(updatedChoice.getWeight());
                    return choiceRepository.save(c);
                }).orElseThrow(() -> new ResourceNotFoundException(
                        "Choice with ID " + updatedChoice.getId() + " for question with ID " + questionId + " not found."
                ));

        // Return the response entity with the updated JSON for the given choice.
        return ResponseEntity.created(new URI("/api/surveys/" + surveyId + "/questions/" + questionId + "/choices" + toReturn.getId()))
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, toReturn.getId().toString()))
                .body(toReturn);
    }

    // I think I need another put request thing here to handle taking in an array of choices to update.

    /**
     * PUT /surveys/{surveyId}/questions/{questionId}/choices :
     * Update a LIST of choices for a given surveyQuestion
     *
     * Note that because this is a put request, any new choices that may be included into the JSON
     * will have to be ignored. New values can be added using a POST request provided by an endpoint
     * defined in the ChoiceResource.
     *
     * @param surveyId Survey that the question is linked to
     * @param questionId Question that the collection of choices to update are linked to
     * @param updatedChoiceList JSON body with all of SET of updated question choices. Set is used to prevent duplicate entries being
     *                          provided.
     * @return Set of the updated choices to confirm the success of the operation.
     * @throws URISyntaxException
     */
    @PutMapping("/surveys/{surveyId}/questions/{questionId}/choices/list")
    @Timed
    public ResponseEntity<Set<Choice>> updateChoicesForSurveyQuestion(@PathVariable(value = "surveyId") Long surveyId,
                                                                       @PathVariable(value = "quesitonId") Long questionId,
                                                                       @RequestBody Set<Choice> updatedChoiceList ) throws URISyntaxException {
        log.debug("PUT request to update SET of choices for question with ID {} for survey with ID {}", questionId, surveyId);
        //Check if the question/survey exists before continuing, no point if it doesn't exist
        if(!questionRepository.existsByIdAndSurveyId(questionId, surveyId))
            throw new ResourceNotFoundException("Question with ID " + questionId + " Survey ID " + surveyId + " not found.");

        /* Utilise streams to process the values to update and build a new with the new values, this method
         * should ignore values that cannot be found.
         */
        Set<Choice> toReturn = updatedChoiceList
                .stream()
                .map(choice -> choiceRepository.findByIdAndQuestionId(choice.getId(), questionId)
                            .map(c -> {
                                c.setChoice(choice.getChoice());
                                c.setWeight(choice.getWeight());
                                return choiceRepository.save(c);
                            }).orElseThrow(() -> new ResourceNotFoundException(
                                "Choice with ID " + choice.getId() + " for question with ID " + questionId + " not found."
                        )))
                .collect(Collectors.toSet());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "setofchoices"))
                .body(toReturn);
    }

    /**
     * GET  /choices : get all the choices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of choices in body
     */
    @GetMapping("/choices")
    @Timed
    public List<Choice> getAllChoices() {
        log.debug("REST request to get all Choices");
        return choiceRepository.findAll();
    }

    /**
     * GET /surveys/{surveyId}/questions/{questionId}/choices :
     * Get all choices for a specified surveyQuestion
     *
     * @param surveyId Survey that the question is linked to
     * @param questionId Question the choices are linked to
     * @return JSON array of all the choices for a surveyQuestion
     */
    @GetMapping("/surveys/{surveyId}/questions/{questionId}/choices")
    @Timed
    public List<Choice> getAllChoicesForSurveyQuestion(@PathVariable(value = "surveyId") Long surveyId,
                                                       @PathVariable(value = "questionId") Long questionId) {
        log.debug("GET request to get all question choices for question with ID {} in survey with ID {}", questionId, surveyId);

        // Check if the surveyQuestion exists, otherwise throw exception to state not found
        if(!questionRepository.existsByIdAndSurveyId(questionId, surveyId))
            throw new ResourceNotFoundException("Question with ID " + questionId + " Survey ID " + surveyId + " not found.");

        return choiceRepository.findByQuestionId(questionId);
    }

    /**
     * GET  /choices/:id : get the "id" choice.
     *
     * @param id the id of the choice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the choice, or with status 404 (Not Found)
     */
    @GetMapping("/choices/{id}")
    @Timed
    public ResponseEntity<Choice> getChoice(@PathVariable Long id) {
        log.debug("REST request to get Choice : {}", id);
        Optional<Choice> choice = choiceRepository.findById(id);
        return (ResponseEntity<Choice>)ResponseUtil.checkNullGetRequest(choice, ENTITY_NAME, id.toString());
    }

    @GetMapping("/surveys/{surveyId}/questions/{questionId}/choices/{choiceId}")
    @Timed
    public ResponseEntity<Choice> getChoiceForSurveyQuestion(@PathVariable(value = "surveyId") Long surveyId,
                                                             @PathVariable(value = "questionId") Long questionId,
                                                             @PathVariable(value = "choiceId") Long choiceId) {
        log.debug("GET request to get choice with ID {} for question with ID {} for survey with ID {}", choiceId, questionId, surveyId);

        // Check if the surveyQuestion exists, otherwise throw exception to state not found
        if(!questionRepository.existsByIdAndSurveyId(questionId, surveyId))
            throw new ResourceNotFoundException("Question with ID " + questionId + " Survey ID " + surveyId + " not found.");

        Optional<Choice> toReturn = choiceRepository.findByIdAndQuestionId(choiceId, questionId);

        if(!toReturn.isPresent())
            throw new ResourceNotFoundException("Choice with ID " + choiceId + " for question with id " + questionId + " not found");

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityGetAlert(ENTITY_NAME, toReturn.get().getId().toString()))
                .body(toReturn.get());
    }

    /**
     * DELETE  /choices/:id : delete the "id" choice.
     *
     * @param id the id of the choice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/choices/{id}")
    @Timed
    public ResponseEntity<Void> deleteChoice(@PathVariable Long id) {
        log.debug("REST request to delete Choice : {}", id);

        choiceRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    //https://stackoverflow.com/questions/2011519/jpa-onetomany-not-deleting-child
    // Choices should be of composition type as they cannot exist without an associated question.
    @DeleteMapping("/surveys/{surveyId}/questions/{questionId}/choices/{choiceId}")
    @Timed
    public ResponseEntity<?> deleteChoiceForSurveyQuestion(@PathVariable(value = "surveyId") Long surveyId,
                                                           @PathVariable(value = "questionId") Long questionId,
                                                           @PathVariable(value = "choiceId") Long choiceId ) {
        log.debug("DELETE request to delete choice with ID {} for question with ID {} for survey with ID {}", choiceId, questionId, surveyId);

        // Check if the surveyQuestion exists, otherwise throw exception to state not found
        if(!questionRepository.existsByIdAndSurveyId(questionId, surveyId))
            throw new ResourceNotFoundException("Question with ID " + questionId + " Survey ID " + surveyId + " not found.");

        // Attempt to delete the question via choiceId and question Id
        choiceRepository.deleteByIdAndQuestionId(choiceId, questionId);

        // Double check that the value has been deleted from the repository, throw exception if found.
        if(choiceRepository.existsByIdAndQuestionId(choiceId, questionId))
            throw new DeletetionFailException("Failed to delete choice with ID " + choiceId + " for question with ID " + questionId);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, choiceId.toString()))
                .build();
    }


}
