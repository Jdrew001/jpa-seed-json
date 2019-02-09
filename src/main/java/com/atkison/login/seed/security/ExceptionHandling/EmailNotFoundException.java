package com.atkison.login.seed.security.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailNotFoundException extends AuthenticationFailedError {
    private static final long serialVersionUID = 1L;

    public EmailNotFoundException(String exception) {
        super(exception);
    }
}
