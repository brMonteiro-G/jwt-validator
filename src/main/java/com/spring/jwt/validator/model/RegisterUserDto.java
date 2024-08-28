package com.spring.jwt.validator.model;


import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {

    @Email
    private String email;

    private String password;

    private String fullName;

}