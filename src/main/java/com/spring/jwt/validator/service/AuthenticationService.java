package com.spring.jwt.validator.service;

import com.spring.jwt.validator.model.LoginUserDto;
import com.spring.jwt.validator.model.RegisterUserDto;
import com.spring.jwt.validator.model.User;
import com.spring.jwt.validator.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String signup(RegisterUserDto input) {
        User user = new User();
                //.setFullName(input.getFullName());
                //.setEmail(input.getEmail())
                //.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.createCustomer(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.getCustomerById(input.getEmail()).get();
    }
}