package com.spring.jwt.validator.exception;

public class OversizeClaimException extends RuntimeException{

    public OversizeClaimException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public OversizeClaimException(String msg) {
        super(msg);
    }
}
