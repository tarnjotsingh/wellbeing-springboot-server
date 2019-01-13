package com.reading.tvirdee.project.springbootdockermysql.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Entity
public class Users {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Indicate that the primary key will be assigned by the persistence provider (e.g. mysql)
    @Column(name = "id")
    private String id;

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

}