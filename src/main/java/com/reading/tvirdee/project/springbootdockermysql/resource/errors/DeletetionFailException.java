package com.reading.tvirdee.project.springbootdockermysql.resource.errors;

import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DeletetionFailException extends RuntimeException  {

    public DeletetionFailException() {
        super();
    }

    public DeletetionFailException(String message) {
        super(message);
    }

    public DeletetionFailException(String message, Throwable cause) {
        super(message, cause);
    }

}
