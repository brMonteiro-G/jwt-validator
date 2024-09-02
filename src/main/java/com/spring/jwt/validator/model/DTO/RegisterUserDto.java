package com.spring.jwt.validator.model.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class RegisterUserDto {

    @Email(message = "email not valid")
    private  String email;

    //TODO: @Pattern that generates a secure password
    private  String password;

    @Size(max = 256, min = 3, message = "name cannot be bigger than 256 characters and lower than 3 characters")
    @Pattern(regexp = "^[A-Za-z]*$", message = "name only can have alphabetic letters")
    private  String fullName;

    private Map<String, String> seed;

    private Map<String, String> role;

    private Map<String, String> name;


}