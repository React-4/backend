package org.pda.announcement.util.security.jwt;

import lombok.Getter;

import javax.security.sasl.AuthenticationException;

@Getter
public class JwtFilterException extends AuthenticationException {

    public JwtErrorCode jwtErrorCode;

    public JwtFilterException(JwtErrorCode jwtErrorCode) {
        this.jwtErrorCode = jwtErrorCode;
    }

}
