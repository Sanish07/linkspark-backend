package com.sanish.url.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegistrationFieldException extends RuntimeException{

    public RegistrationFieldException(String message) {
        super(message);
    }
}
