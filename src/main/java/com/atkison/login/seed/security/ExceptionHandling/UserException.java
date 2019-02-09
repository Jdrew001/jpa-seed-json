package com.atkison.login.seed.security.ExceptionHandling;

public class UserException extends RuntimeException {
    public UserException(String exception)
    {
        super(exception);
    }
}
