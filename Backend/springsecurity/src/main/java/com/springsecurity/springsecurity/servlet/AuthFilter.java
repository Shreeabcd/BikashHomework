package com.springsecurity.springsecurity.servlet;

import com.springsecurity.springsecurity.entity.UserAuthToken;
import com.springsecurity.springsecurity.exception.AuthorizationFailedException;
import com.springsecurity.springsecurity.exception.RestErrorCode;
import com.springsecurity.springsecurity.exception.UnauthorizedException;
import com.springsecurity.springsecurity.provider.BearerAuthDecoder;
import com.springsecurity.springsecurity.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.springsecurity.springsecurity.constants.ResourceConstants.BASIC_AUTH_PREFIX;

@Component
public class AuthFilter extends ApiFilter {

    @Autowired
    private AuthTokenService authTokenService;

    @Override
    public void doFilter(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (servletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            servletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        final String pathInfo = servletRequest.getRequestURI();
        if (!pathInfo.contains("register") && !pathInfo.contains("actuator") ) {
            final String authorization = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isEmpty(authorization)) {
                throw new UnauthorizedException(RestErrorCode.ATH_001);
            }

            if (pathInfo.contains("login") && !authorization.startsWith(BASIC_AUTH_PREFIX)) {
                throw new UnauthorizedException(RestErrorCode.ATH_002);
            }

            if (!pathInfo.contains("login")) {
                final String accessToken = new BearerAuthDecoder(authorization).getAccessToken();
                try {
                    final UserAuthToken userAuthTokenEntity = authTokenService.validateToken(accessToken);
                    servletRequest.setAttribute(HttpHeaders.AUTHORIZATION, userAuthTokenEntity.getUserDetails());
                } catch (AuthorizationFailedException e) {
                    servletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
                    return;
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}

