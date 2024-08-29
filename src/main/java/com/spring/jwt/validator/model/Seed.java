package com.spring.jwt.validator.model;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Seed {

    @Pattern(regexp = "^[A-Za-z]*$", message = "Password must contain at least one uppercase letter and one number")
    private String seed;

}
