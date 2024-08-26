package com.spring.jwt.validator.controller;

import com.spring.jwt.validator.model.*;
import com.spring.jwt.validator.service.AuthenticationService;
import com.spring.jwt.validator.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "v1/base")
@RequiredArgsConstructor
public class JwtValidadorController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    @PostMapping("/persist")
    public String myFisrtController(@RequestBody JwtDTO jwt) {

        return "it works";
    }

    @GetMapping("/retrieve")
    public String myFisrtControllerGet() {
        return "it works too";

    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        //authenticationService.signup(registerUserDto);
        User registeredUser = new User(12, "", "", "", LocalDate.now(), LocalDate.now());

        return ResponseEntity.ok(registeredUser);
    }


}
