package com.springsecurity.springsecurity.service;

import com.springsecurity.springsecurity.entity.UserAuthToken;
import com.springsecurity.springsecurity.entity.UserDetails;
import com.springsecurity.springsecurity.exception.AuthorizationFailedException;
import com.springsecurity.springsecurity.exception.UserErrorCode;
import com.springsecurity.springsecurity.provider.JwtTokenProvider;
import com.springsecurity.springsecurity.repository.UserAuthTokenRepository;
import com.springsecurity.springsecurity.repository.UserDetailsRepository;
import com.springsecurity.springsecurity.utils.DateTimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.rmi.UnexpectedException;
import java.time.ZonedDateTime;

@Service
public class AuthTokenService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserAuthTokenRepository userAuthDao;


    @Transactional(propagation = Propagation.MANDATORY)
    public UserAuthToken issueToken(final UserDetails userDetails) throws UnexpectedException {

        final ZonedDateTime now = DateTimeProvider.currentProgramTime();

        final UserAuthToken userAuthToken = userAuthDao.findByUserUserName(userDetails.getUserName());
        final UserAuthTokenVerifier tokenVerifier = new UserAuthTokenVerifier(userAuthToken);
        if (tokenVerifier.isActive()) {
            return userAuthToken;
        }

        final JwtTokenProvider tokenProvider = new JwtTokenProvider(userDetails.getPassword());
        final ZonedDateTime expiresAt = now.plusMinutes(1);
        final String authToken = tokenProvider.generateToken(userDetails.getEmailId(), now, expiresAt);
        System.out.println(authToken);
        final UserAuthToken authTokenEntity = new UserAuthToken();
        authTokenEntity.setUserDetails(userDetails);
        authTokenEntity.setAccessToken(authToken);
        authTokenEntity.setLoginAt(now);
        authTokenEntity.setExpiresAt(expiresAt);
        userAuthDao.save(authTokenEntity);

        return authTokenEntity;

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void invalidateToken(final String accessToken) throws AuthorizationFailedException {

        final UserAuthToken userAuthToken = userAuthDao.findByAccessToken(accessToken);
        final UserAuthTokenVerifier tokenVerifier = new UserAuthTokenVerifier(userAuthToken);
        if (tokenVerifier.isNotFound()) {
            throw new AuthorizationFailedException(UserErrorCode.USR_005);
        }
        if (tokenVerifier.hasExpired()) {
            throw new AuthorizationFailedException(UserErrorCode.USR_006);
        }

        userAuthToken.setLogoutAt(DateTimeProvider.currentProgramTime());
        userAuthDao.save(userAuthToken);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthToken validateToken(@NotNull String accessToken) throws AuthorizationFailedException {
        final UserAuthToken userAuthToken = userAuthDao.findByAccessToken(accessToken);
        final UserAuthTokenVerifier tokenVerifier = new UserAuthTokenVerifier(userAuthToken);
        if (tokenVerifier.isNotFound() || tokenVerifier.hasLoggedOut()) {
            throw new AuthorizationFailedException(UserErrorCode.USR_005);
        }
        if (tokenVerifier.hasExpired()) {
            throw new AuthorizationFailedException(UserErrorCode.USR_006);
        }
        return userAuthToken;
    }

}
