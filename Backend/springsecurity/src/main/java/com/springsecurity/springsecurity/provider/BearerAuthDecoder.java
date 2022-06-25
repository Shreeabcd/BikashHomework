package com.springsecurity.springsecurity.provider;

import com.springsecurity.springsecurity.exception.RestErrorCode;
import com.springsecurity.springsecurity.exception.UnauthorizedException;

import static com.springsecurity.springsecurity.constants.ResourceConstants.BEARER_AUTH_PREFIX;

/**
 * Provider to decode bearer token.
 */
public class BearerAuthDecoder {

    private final String accessToken;

    public BearerAuthDecoder(final String bearerToken) {
        if (!bearerToken.startsWith(BEARER_AUTH_PREFIX)) {
            throw new UnauthorizedException(RestErrorCode.ATH_003);
        }

        final String[] bearerTokens = bearerToken.split(BEARER_AUTH_PREFIX);
        if (bearerTokens.length != 2) {
            throw new UnauthorizedException(RestErrorCode.ATH_004);
        }
        this.accessToken = bearerTokens[1];
    }

    public String getAccessToken() {
        return accessToken;
    }

}