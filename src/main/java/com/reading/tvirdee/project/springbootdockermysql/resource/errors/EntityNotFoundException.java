package com.reading.tvirdee.project.springbootdockermysql.resource.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class EntityNotFoundException extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public final ResponseEntity<ErrorD>
}
