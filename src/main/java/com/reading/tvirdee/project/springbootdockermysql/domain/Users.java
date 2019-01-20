package com.reading.tvirdee.project.springbootdockermysql.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "users")
public class Users implements Serializable{

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Indicate that the primary key will be assigned by the persistence provider (e.g. mysql)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "role")
    private String role;

    @NotNull
    @Column(name = "login")
    private String login;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(name = "email", length = 254)
    private String email;

    //Don't want the password hash being returned in a json response
    //Use spring security password encoder to generate a hash for the user.
//    @JsonIgnore
//    @NotNull
//    @Size(min = 60, max = 60)
//    @Column(name = "password_hash", length = 60, nullable = true)
//    private String password;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}