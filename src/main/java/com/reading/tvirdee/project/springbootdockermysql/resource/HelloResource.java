package com.reading.tvirdee.project.springbootdockermysql.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
public class HelloResource {

    @GetMapping
    public String helloWorld() {
        return "Hello World!";
    }
}
