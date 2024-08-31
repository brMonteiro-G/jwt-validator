package com.spring.jwt.validator.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDto {

    private String role;

    private String seed;

    private String name;

}
