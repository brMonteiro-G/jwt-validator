package com.spring.jwt.validator.controller;

import com.spring.jwt.validator.model.*;
import com.spring.jwt.validator.model.DTO.LoginUserDto;
import com.spring.jwt.validator.model.DTO.RegisterUserDto;
import com.spring.jwt.validator.model.DTO.UserDTO;
import com.spring.jwt.validator.service.AuthenticationService;
import com.spring.jwt.validator.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "v1/base")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JwtValidadorController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;


    // get allow list
    @GetMapping("/retrieve")
    // to do  @PreAuthorize("isFullyAuthenticated() and hasAuthority('admin')")
    public String myFisrtControllerGet() {
        return "it works too";

    }

    // login new user and validate jwt generated
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginUserDto loginUserDto) {
        UserDTO authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }



    // register new user
    @PostMapping("/signup")
    // to do adicionar response DTO
    public ResponseEntity<String> register(@RequestBody @Valid RegisterUserDto registerUserDto) {

        var registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }


}
