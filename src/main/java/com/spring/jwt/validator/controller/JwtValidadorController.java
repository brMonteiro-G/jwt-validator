package com.spring.jwt.validator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1/base")
public class JwtValidadorController {

    @PostMapping("/persist")
    public String myFisrtController() {

        return "it works";
    }

    @GetMapping("/retrieve")
    public String myFisrtControllerGet() {
        return "it works too";

    }

}
