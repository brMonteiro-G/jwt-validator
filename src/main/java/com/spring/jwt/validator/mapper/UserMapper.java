package com.spring.jwt.validator.mapper;

import com.spring.jwt.validator.model.User;
import com.spring.jwt.validator.model.UserDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@NoArgsConstructor
@Component
public class UserMapper {

    public static User convert(UserDTO userDTO) {

        return User.builder()
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .id(UUID.randomUUID().toString())
                .password(userDTO.getPassword()).build();

    }

    public static UserDTO convertUserToUserDTO(User user) {

        return UserDTO.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .password(user.getPassword()).build();

    }


}
