package com.spring.jwt.validator.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.spring.jwt.validator.model.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class UserRepository {
    final private DynamoDBMapper dynamoDBMapper;

    public String createCustomer(User customer) {
        dynamoDBMapper.save(customer);
        return customer.getEmail();
    }

    public User getCustomerById(String id) {
        return dynamoDBMapper.load(User.class, id);
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
        return load.getId() + " get deleted !";
    }
}