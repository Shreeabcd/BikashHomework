package com.springsecurity.springsecurity.controller;

import com.springsecurity.springsecurity.exception.ApplicationException;
import com.springsecurity.springsecurity.model.AuthorizedUser;
import com.springsecurity.springsecurity.provider.BasicAuthDecoder;
import com.springsecurity.springsecurity.provider.BearerAuthDecoder;
import com.springsecurity.springsecurity.service.AuthTokenService;
import com.springsecurity.springsecurity.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.UnexpectedException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthTokenService authTokenService;

    @PostMapping(path = "/login")
    public ResponseEntity<AuthorizedUser> login(@RequestHeader final String authorization) throws ApplicationException, UnexpectedException {
        final BasicAuthDecoder basicAuthDecoder = new BasicAuthDecoder(authorization);
        final AuthorizedUser authorizedUser = authenticationService.authenticate(basicAuthDecoder.getEmail(), basicAuthDecoder.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(authorizedUser);
    }

    @PostMapping(path = "/logout")
    public void logout(@RequestHeader final String authorization) throws ApplicationException {
        final BearerAuthDecoder authDecoder = new BearerAuthDecoder(authorization);
        authTokenService.invalidateToken(authDecoder.getAccessToken());
    }


}
