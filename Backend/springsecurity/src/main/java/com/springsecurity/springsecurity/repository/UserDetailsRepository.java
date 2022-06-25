package com.springsecurity.springsecurity.repository;

import com.springsecurity.springsecurity.entity.UserDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDetailsRepository extends CrudRepository<UserDetails, String> {


    @Override
    List<UserDetails> findAll();

    //specify a method that returns User by finding it by userName

    UserDetails findByEmailId(String userName);
}
