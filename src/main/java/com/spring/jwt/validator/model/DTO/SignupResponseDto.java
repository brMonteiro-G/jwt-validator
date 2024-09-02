package com.spring.jwt.validator.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SignupResponseDto {

    private String message;
    private String email;
    private List<String> access;


}
