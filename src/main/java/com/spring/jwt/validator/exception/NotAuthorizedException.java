package com.spring.jwt.validator.exception;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(final Exception e){
        super(e);
    }
}
