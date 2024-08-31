package com.spring.jwt.validator.model.DTO;

import lombok.Data;

@Data
public class ErrorDto {

    private String message;
    private String stacktrace;

}
