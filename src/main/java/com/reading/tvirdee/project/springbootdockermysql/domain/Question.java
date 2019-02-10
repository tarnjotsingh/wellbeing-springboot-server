package com.reading.tvirdee.project.springbootdockermysql.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Question object for populating the database with.
 * Also maps out it's relationship to other objects such as Survey and
 * UserQuestionChoice.
 */
@Entity
@Table(name = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question")
    private String question;

    // One to Many relationship of the question choices to this question.
    @OneToMany(mappedBy = "question")
    private Set<Choice> questionChoices = new HashSet<>();

    // Need to ignore the questions property in the Survey class to prevent infinite recursion happening on a GET request.
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonIgnore
    private Survey survey;

    @OneToMany(mappedBy = "question")
    private Set<UserQuestionChoice> userQuestionChoices = new HashSet<>();


    /*------------------------------------------------------------------------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public Question question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    /*----------------- Manage choices to this question -------------------*/

    public Set<Choice> getQuestionChoices() {
        return this.questionChoices;
    }

    public Question choices(Set<Choice> questionChoices) {
        this.questionChoices = questionChoices;
        return this;
    }

    public Question addQuestionChoice(Choice questionChoice) {
        this.questionChoices.add(questionChoice);
        questionChoice.setQuestion(this);
        return this;
    }

    public Question removeQuestionChoice(Choice questionChoice) {
        this.questionChoices.remove(questionChoice);
        questionChoice.setQuestion(null);
        return this;
    }

    public void setQuestionChoices(Set<Choice> questionChoices) {
        this.questionChoices = questionChoices;
    }

    /* --------------------------- Survey -----------------------------*/

    public Survey getSurvey() {
        return survey;
    }

    public Question survey(Survey survey) {
        this.survey = survey;
        return this;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }


    /*----------------------------- User question choices ----------------------------------*/

    public Set<UserQuestionChoice> getUserQuestionChoices() {
        return userQuestionChoices;
    }

    public Question userQuestionChoices(Set<UserQuestionChoice> userQuestionChoices) {
        this.userQuestionChoices = userQuestionChoices;
        return this;
    }

    public Question addUserQuestionChoice(UserQuestionChoice userQuestionChoice) {
        this.userQuestionChoices.add(userQuestionChoice);
        userQuestionChoice.setQuestion(this);
        return this;
    }

    public Question removeUserQuestionChoice(UserQuestionChoice userQuestionChoice) {
        this.userQuestionChoices.remove(userQuestionChoice);
        userQuestionChoice.setQuestion(null);
        return this;
    }

    public void setUserQuestionChoices(Set<UserQuestionChoice> userQuestionChoices) {
        this.userQuestionChoices = userQuestionChoices;
    }

    /*---------------------------- Util -------------------------------*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        if (question.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            "}";
    }
}
