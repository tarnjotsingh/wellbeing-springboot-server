package com.reading.tvirdee.project.springbootdockermysql.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MultipleAuthResource {

    @GetMapping("/api/ping")
    public String getPing() {
        return "OK";
    }
}
