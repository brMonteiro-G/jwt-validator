package com.spring.jwt.validator.model;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Name {

    @Size(max = 256, min = 3, message = "name cannot be bigger than 256 characters and lower than 3 characters")
    private String name;
}
