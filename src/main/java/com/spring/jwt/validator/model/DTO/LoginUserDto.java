package com.spring.jwt.validator.model.DTO;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginUserDto {

    @Email(message = "email not valid")
    private String email;

    private String password;

}