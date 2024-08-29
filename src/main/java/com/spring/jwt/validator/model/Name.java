package com.spring.jwt.validator.model;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Name {

    @Size(max = 256, message = "message can only have", min = 3)
    private String name;
}
