package com.spring.jwt.validator.mapper;

import com.spring.jwt.validator.model.DTO.RegisterUserDto;
import com.spring.jwt.validator.model.DTO.UserDTO;
import com.spring.jwt.validator.model.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@NoArgsConstructor
@Component
public class UserMapper {

    public static User convert(RegisterUserDto userDTO) {

        return User.builder()
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .id(UUID.randomUUID().toString())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .seed(userDTO.getSeed())
                .name(userDTO.getName())
                .build();

    }

    public static User convert(UserDTO userDTO) {

        return User.builder()
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .id(UUID.randomUUID().toString())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .seed(userDTO.getSeed())
                .name(userDTO.getName())
                .build();

    }

    public static UserDTO convertUserToUserDTO(User user) {

        return UserDTO.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .password(user.getPassword())
                .role(user.getRole())
                .seed(user.getSeed())
                .name(user.getName())
                .build();

    }


}
