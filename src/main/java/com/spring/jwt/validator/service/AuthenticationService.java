package com.spring.jwt.validator.service;

import com.spring.jwt.validator.exceptions.UserNotFoundException;
import com.spring.jwt.validator.mapper.UserMapper;
import com.spring.jwt.validator.model.DTO.LoginUserDto;
import com.spring.jwt.validator.model.DTO.RegisterUserDto;
import com.spring.jwt.validator.model.DTO.UserDTO;
import com.spring.jwt.validator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;



    public String signup(RegisterUserDto registerUserDto) {
        UserDTO newUser = new UserDTO(registerUserDto.getFullName(), registerUserDto.getEmail(), passwordEncoder.encode(registerUserDto.getPassword()), "category");
        var user = UserMapper.convert(newUser);
        userRepository.createCustomer(user);
        return user.getEmail();
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
}