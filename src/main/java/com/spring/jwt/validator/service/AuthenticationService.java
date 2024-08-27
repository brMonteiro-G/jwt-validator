package com.spring.jwt.validator.service;

import com.spring.jwt.validator.mapper.UserMapper;
import com.spring.jwt.validator.model.LoginUserDto;
import com.spring.jwt.validator.model.RegisterUserDto;
import com.spring.jwt.validator.model.User;
import com.spring.jwt.validator.model.UserDTO;
import com.spring.jwt.validator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;

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
        UserDTO userdto = new UserDTO(input.getFullName(), input.getEmail(), input.getPassword(), "");
        //.setFullName(input.getFullName());
        //.setEmail(input.getEmail())
        //.setPassword(passwordEncoder.encode(input.getPassword()));
        var user = UserMapper.convert(userdto);
        return userRepository.createCustomer(user);
    }

    public UserDTO authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        var user = userRepository.getCustomerById(input.getEmail()).get();
        var newUser = UserMapper.convert(user);
        return UserMapper.convertUserToUserDTO(newUser);
    }
}