package com.spring.jwt.validator.fixture;

import com.spring.jwt.validator.enums.AllowedRoles;
import com.spring.jwt.validator.model.DTO.LoginUserDto;
import com.spring.jwt.validator.model.DTO.RegisterUserDto;
import com.spring.jwt.validator.model.DTO.SignupResponseDto;
import com.spring.jwt.validator.model.DTO.UserDTO;
import com.spring.jwt.validator.model.LoginResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ObjectsFixture {

    public static RegisterUserDto registerUserDtoFixture() {
        Map<String, String> seed = new HashMap<>();
        Map<String, String> name = new HashMap<>();
        Map<String, String> role = new HashMap<>();

        role.put("ROLE", AllowedRoles.ADMIN.value);
        seed.put("Seed", String.valueOf(17));
        name.put("Name", "Gabriel");

        return RegisterUserDto.builder().email("brgabriel.monteiro@hotmail.com").fullName("Gabriel").seed(seed).role(role).password("some-strange-password").name(name).build();
    }

    public static SignupResponseDto signupResponseFixture() {

        return SignupResponseDto.builder().email("brgabriel.monteiro@hotmail.com").message("User created with success").access(List.of("write-app-extrato", "admin-datadog")).build();
    }

    public static LoginResponse loginResponseFixture() {

        return LoginResponse.builder().token("eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiYWRtaW4iLCJTZWVkIjoiOTciLCJOYW1lIjoiZ2FicmllbCJ9.Nv9adYfdQhMHpYVwetWCD6n6sLFKmsXmjUzBiQiDiAE").expiresIn(360000).build();
    }

    public static LoginUserDto loginUserFixture() {

        return LoginUserDto.builder().email("brgabriel.monteiro@hotmail.com").password("test").build();
    }

    public static UserDTO userDtoFixture() {

        return UserDTO.builder().email("brgabriel.monteiro@hotmail.com").password("test").fullName("Gabriel").build();
    }

}
