package com.reading.tvirdee.project.springbootdockermysql.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserQuestionChoice.
 */
@Entity
@Table(name = "user_question_choice")
public class UserQuestionChoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Users
     */
    @ManyToOne
    private Users user;

    @Column(name = "time_stamp")
    private Long timeStamp;

    @ManyToOne
    @JsonIgnoreProperties("userQuestionChoices")
    private Question question;

    @ManyToOne
    @JsonIgnoreProperties("userQuestionChoices")
    private Choice choice;

    /*--------------------Getter/Setter--------------------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public UserQuestionChoice timeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Question getQuestion() {
        return question;
    }

    public UserQuestionChoice question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Choice getChoice() {
        return choice;
    }

    public UserQuestionChoice choice(Choice choice) {
        this.choice = choice;
        return this;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserQuestionChoice userQuestionChoice = (UserQuestionChoice) o;
        if (userQuestionChoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userQuestionChoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserQuestionChoice{" +
            "id=" + getId() +
            ", timeStamp=" + getTimeStamp() +
            "}";
    }
}
