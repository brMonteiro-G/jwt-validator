package com.spring.jwt.validator.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    private String email;

    private String password;

    // getters and setters here...
}