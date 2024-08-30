package com.spring.jwt.validator.exception;

public class InvalidClaimNameException extends RuntimeException {
    public InvalidClaimNameException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidClaimNameException(String msg) {
        super(msg);
    }
}
