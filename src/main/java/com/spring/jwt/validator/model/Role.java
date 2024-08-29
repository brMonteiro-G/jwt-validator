package com.spring.jwt.validator.model;

import com.spring.jwt.validator.enums.AllowedRoles;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Role {

    private AllowedRoles role;
}
