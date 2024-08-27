package com.spring.jwt.validator.exceptions;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(final Exception e){
        super(e);
    }
}
