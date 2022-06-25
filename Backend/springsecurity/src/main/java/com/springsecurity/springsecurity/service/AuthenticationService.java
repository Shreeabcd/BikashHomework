package com.springsecurity.springsecurity.service;

import com.springsecurity.springsecurity.entity.UserAuthToken;
import com.springsecurity.springsecurity.entity.UserDetails;
import com.springsecurity.springsecurity.exception.ApplicationException;
import com.springsecurity.springsecurity.exception.AuthenticationFailedException;
import com.springsecurity.springsecurity.exception.UserErrorCode;
import com.springsecurity.springsecurity.model.AuthorizedUser;
import com.springsecurity.springsecurity.provider.PasswordCryptographyProvider;
import com.springsecurity.springsecurity.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.UnexpectedException;

@Service
public class AuthenticationService {

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private UserDetailsRepository userDetailsRepository;


    @Transactional(propagation = Propagation.REQUIRED)
    public AuthorizedUser authenticate(final String email, final String password) throws ApplicationException, UnexpectedException {

        UserDetails userDetails = userDetailsRepository.findByEmailId(email);
        if (userDetails == null) throw new AuthenticationFailedException(UserErrorCode.USR_002);

        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, userDetails.getSalt());
        if (!userDetails.getPassword().equals(encryptedPassword)) {

            throw new AuthenticationFailedException(UserErrorCode.USR_003);
        }
        UserAuthToken userAuthToken = authTokenService.issueToken(userDetails);
        return authorizedUser(userDetails, userAuthToken);


    }

    private AuthorizedUser authorizedUser(final UserDetails userDetails, final UserAuthToken userAuthToken) {
        final AuthorizedUser authorizedUser = new AuthorizedUser();
        authorizedUser.setId(userDetails.getUserName());
        authorizedUser.setFirstName(userDetails.getFirstName());
        authorizedUser.setLastName(userDetails.getLastName());
        authorizedUser.setEmailAddress(userDetails.getEmailId());
        authorizedUser.setMobilePhoneNumber(userDetails.getMobile());
        authorizedUser.setAccessToken(userAuthToken.getAccessToken());

        return authorizedUser;
    }

}

