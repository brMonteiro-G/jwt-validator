package com.spring.jwt.validator.controller;

import com.spring.jwt.validator.model.*;
import com.spring.jwt.validator.repository.UserRepository;
import com.spring.jwt.validator.service.AuthenticationService;
import com.spring.jwt.validator.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping(path = "v1/base")
@RequiredArgsConstructor
public class JwtValidadorController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;


    // get allow list
    @GetMapping("/retrieve")
    public String myFisrtControllerGet() {
        return "it works too";

    }

    // login new user and validate jwt generated
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        UserDTO authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }

    // register new user
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        //authenticationService.signup(registerUserDto);
        User registeredUser = new User(UUID.randomUUID().toString(),registerUserDto.getFullName(), registerUserDto.getEmail(), registerUserDto.getPassword());
        userRepository.createCustomer(registeredUser);
        return ResponseEntity.ok(registeredUser);
    }


}
