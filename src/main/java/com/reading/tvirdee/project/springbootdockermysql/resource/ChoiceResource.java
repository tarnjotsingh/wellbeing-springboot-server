package com.reading.tvirdee.project.springbootdockermysql.resource;

import com.codahale.metrics.annotation.Timed;
import com.reading.tvirdee.project.springbootdockermysql.domain.Choice;
import com.reading.tvirdee.project.springbootdockermysql.repository.ChoiceRepository;
import com.reading.tvirdee.project.springbootdockermysql.resource.errors.BadRequestAlertException;
import com.reading.tvirdee.project.springbootdockermysql.resource.util.HeaderUtil;
//import io.github.jhipster.web.util.ResponseUtil;

import com.reading.tvirdee.project.springbootdockermysql.resource.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Choice.
 */
@RestController
@RequestMapping("/api")
public class ChoiceResource {

    private final Logger log = LoggerFactory.getLogger(ChoiceResource.class);

    private static final String ENTITY_NAME = "choice";

    private final ChoiceRepository choiceRepository;

    public ChoiceResource(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
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
}
