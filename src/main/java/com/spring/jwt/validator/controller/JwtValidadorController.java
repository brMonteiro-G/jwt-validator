package com.spring.jwt.validator.controller;

import com.spring.jwt.validator.model.JwtDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "v1/base")
public class JwtValidadorController {

    @PostMapping("/persist")
    public String myFisrtController(@RequestBody JwtDTO jwt) {

        return "it works";
    }

    @GetMapping("/retrieve")
    public String myFisrtControllerGet() {
        return "it works too";

    }

}
