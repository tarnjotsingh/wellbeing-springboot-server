package com.reading.tvirdee.project.springbootdockermysql.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Survey.
 */
@Entity
@Table(name = "survey")
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "description")
    private String description;

    // Questions in a one to many relationship mapped by survey as it is basically the foreign in the Question class.
    @OneToMany(mappedBy = "survey")
    @JsonIgnore
    private Set<Question> questions = new HashSet<>();

    /*---------------------------Getter/Setter-------------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() {
        return description;
    }

    public Survey description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*------------------- Questions -----------------------*/

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public Survey surveys(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Survey addQuestion(Question question) {
        this.questions.add(question);
        question.setSurvey(this);
        return this;
    }

    public Survey removeQuestion(Question question) {
        this.questions.remove(question);
        question.setSurvey(null);
        return this;
    }

    public void setSurveys(Set<Question> questions) {
        this.questions = questions;
    }

    /*---------------------------Util----------------------*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Survey survey = (Survey) o;
        if (survey.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), survey.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Survey{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
