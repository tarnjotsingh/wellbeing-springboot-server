package com.reading.tvirdee.project.springbootdockermysql.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Choice.
 */
@Entity
@Table(name = "choice")
public class Choice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "choice")
    private String choice;

    @Column(name = "weight")
    private Integer weight;

    @OneToMany(mappedBy = "choice")
    @JsonIgnore
    private Set<UserQuestionChoice> userQuestionChoices = new HashSet<>();

    // Need to ignore questionChoices to stop an infinite recursion happening when doing a GET request
    @ManyToOne
    @JsonIgnore
    private Question question;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public Choice choice(String choice) {
        this.choice = choice;
        return this;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public Integer getWeight() {
        return weight;
    }

    public Choice weight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Set<UserQuestionChoice> getUserQuestionChoices() {
        return userQuestionChoices;
    }

    public Choice userQuestionChoices(Set<UserQuestionChoice> userQuestionChoices) {
        this.userQuestionChoices = userQuestionChoices;
        return this;
    }

    public Choice addUserQuestionChoice(UserQuestionChoice userQuestionChoice) {
        this.userQuestionChoices.add(userQuestionChoice);
        userQuestionChoice.setChoice(this);
        return this;
    }

    public Choice removeUserQuestionChoice(UserQuestionChoice userQuestionChoice) {
        this.userQuestionChoices.remove(userQuestionChoice);
        userQuestionChoice.setChoice(null);
        return this;
    }

    public void setUserQuestionChoices(Set<UserQuestionChoice> userQuestionChoices) {
        this.userQuestionChoices = userQuestionChoices;
    }

    public Question getQuestion() {
        return question;
    }

    public Choice question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Choice choice = (Choice) o;
        if (choice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), choice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Choice{" +
            "id=" + getId() +
            ", choice='" + getChoice() + "'" +
            ", weight=" + getWeight() +
            "}";
    }
}
