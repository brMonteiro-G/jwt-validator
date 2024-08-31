package com.spring.jwt.validator.model.DTO;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {

    @Email(message = "email not valid")
    private String email;

    private String password;

    // getters and setters here...
}