package com.spring.jwt.validator.service;

import com.spring.jwt.validator.enums.AllowedRoles;
import com.spring.jwt.validator.exception.UserNotFoundException;
import com.spring.jwt.validator.mapper.UserMapper;
import com.spring.jwt.validator.model.DTO.LoginUserDto;
import com.spring.jwt.validator.model.DTO.RegisterUserDto;
import com.spring.jwt.validator.model.DTO.SignupResponseDto;
import com.spring.jwt.validator.model.DTO.UserDTO;
import com.spring.jwt.validator.model.User;
import com.spring.jwt.validator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.primes.Primes;

import java.util.List;
import java.util.Random;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public SignupResponseDto signup(RegisterUserDto registerUserDto) {

        var role = this.grantClaims(registerUserDto);
        var name = this.grantName(registerUserDto);
        var seed = this.grantSeed();
        registerUserDto.setRole(role);
        registerUserDto.setSeed(seed);
        registerUserDto.setName(name);
        registerUserDto.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        User user = UserMapper.convert(registerUserDto);
        userRepository.createCustomer(user);

        return new SignupResponseDto("User created with success", user.getEmail(), List.of("app-extrato-1", "datadog-app-extrato"));
    }

    public UserDTO authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        var user = userRepository.getCustomerById(input.getEmail()).orElseThrow(UserNotFoundException::new);
        var newUser = UserMapper.convert(user);
        return UserMapper.convertUserToUserDTO(newUser);
    }

    public List<User> getAllAccess() {

        return userRepository.getAllUsers();

    }

    private Map<String, String> grantClaims(RegisterUserDto user) {

        Map<String, String> claims = new HashMap<>();
        var role = "ROLE";

        var email = user.getEmail();

        if (email.contains("@gmail")) {
            claims.put(role, AllowedRoles.ADMIN.value);
        } else if (email.contains("@hotmail")) {
            claims.put(role, AllowedRoles.MEMBER.value);
        } else {
            claims.put(role, AllowedRoles.EXTERNAL.value);
        }

        return claims;

    }
//TODO: Don´t forget to fix it
    public Map<String, String> grantSeed() {

        Map<String, String> seed = new HashMap<>();
        Random random = new Random();

        int prime = Primes.nextPrime(random.nextInt(100));
        seed.put("Seed", String.valueOf(prime));

        return seed;
    }

    private Map<String, String> grantName(RegisterUserDto registerUserDto) {
        Map<String, String> map = new HashMap<>();
        map.put("Name", registerUserDto.getFullName());
        return map;
    }


}