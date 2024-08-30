package com.spring.jwt.validator.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Seed {

    @Pattern(regexp = "^[A-Za-z]*$", message = "seed")
    private String seed;

}
