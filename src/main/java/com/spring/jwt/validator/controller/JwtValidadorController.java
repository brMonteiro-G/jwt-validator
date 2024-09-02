package com.spring.jwt.validator.controller;

import com.spring.jwt.validator.model.AllowedResourcesResponse;
import com.spring.jwt.validator.model.DTO.LoginUserDto;
import com.spring.jwt.validator.model.DTO.RegisterUserDto;
import com.spring.jwt.validator.model.DTO.SignupResponseDto;
import com.spring.jwt.validator.model.DTO.UserDTO;
import com.spring.jwt.validator.model.LoginResponse;
import com.spring.jwt.validator.service.AuthenticationService;
import com.spring.jwt.validator.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "v1/base")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JwtValidadorController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;


    // get allow list
    @GetMapping("/retrieve")
    // @PreAuthorize("isFullyAuthenticated() and hasRole('admin')")
    public ResponseEntity<AllowedResourcesResponse> getAllowList() {
        var allAccess = authenticationService.getAllAccess();
        AllowedResourcesResponse response = new AllowedResourcesResponse();
        response.setAllowedResources(List.of("write-app-2", "read-extrato-1"));
        response.setListUser(allAccess);

        return ResponseEntity.ok(response);

    }

    // login new user and validate jwt generated
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginUserDto loginUserDto) {

        UserDTO user = authenticationService.authenticate(loginUserDto);
        log.info("user found in database {}", user.getName());

        LoginResponse authenticatedUser = jwtService.generatedAuthenticatedUser(user);
        log.info("retrieving token for user {}", user.getName());
        return ResponseEntity.ok(authenticatedUser);
    }


    // register new user
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> register(@RequestBody @Valid RegisterUserDto registerUserDto) {

        SignupResponseDto registeredUser = authenticationService.signup(registerUserDto);
        log.info("register user in database {}", registeredUser.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }


}
