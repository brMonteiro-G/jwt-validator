package com.spring.jwt.validator.model;

import com.spring.jwt.validator.model.DTO.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class AllowedResourcesResponse {

    private List<String> allowedResources;
    private  List<User> listUser;

}
