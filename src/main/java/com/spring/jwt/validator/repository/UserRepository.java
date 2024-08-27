package com.spring.jwt.validator.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.spring.jwt.validator.mapper.UserMapper;
import com.spring.jwt.validator.model.User;
import com.spring.jwt.validator.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
@EnableScan
public class UserRepository {

    private final DynamoDBMapper dynamoDBMapper;


    public String createCustomer(User customer) {

        dynamoDBMapper.save(customer);
        return customer.getEmail();
    }

    public Optional<UserDTO> getCustomerById(String id) {

       // var userFromDatabase = dynamoDBMapper.scan(User.class);
        //TODO: findByEmail using query
        List<User> scanResult = new ArrayList<>(dynamoDBMapper.scan(User.class, new DynamoDBScanExpression()));

        var user = UserMapper.convertUserToUserDTO(scanResult.stream().filter(element -> Objects.equals(element.getEmail(), id)).findFirst().get());

        return Optional.ofNullable(user);
    }

    public User updateCustomer(String id, User customer) {
        User load = dynamoDBMapper.load(User.class, id);
        // map these two entity
        log.info("my test");
        dynamoDBMapper.save(load);

        return dynamoDBMapper.load(User.class, id);
    }

    public String deleteCustomer(String id) {
        User load = dynamoDBMapper.load(User.class, id);
        dynamoDBMapper.delete(load);
        return load.getEmail()+ " get deleted !";
    }
}