package com.springsecurity.springsecurity.repository;

import com.springsecurity.springsecurity.entity.UserAuthToken;

import com.sun.istack.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAuthTokenRepository extends  CrudRepository<UserAuthToken, String> {

        UserAuthToken findByUserUserName(@NotNull String userName);

        UserAuthToken findByAccessToken(String token);

        }

