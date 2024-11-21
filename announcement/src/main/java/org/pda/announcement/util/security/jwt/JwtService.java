package org.pda.announcement.util.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.pda.announcement.util.security.jwt.JwtRequestFilter.HEADER_STRING;
import static org.pda.announcement.util.security.jwt.JwtRequestFilter.TOKEN_PREFIX;

/**
 * JWT 토큰 생성 및 검증을 위한 서비스
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    /**
     * JWT 토큰 복호화
     *
     * @param request HttpServletRequest
     * @return 사용자 이메일
     */
    public String getUserEmailByJWT(HttpServletRequest request) {
        String jwtHeader = request.getHeader(HEADER_STRING);
        String token = jwtHeader.replace(TOKEN_PREFIX, "");
        return JWT.require(Algorithm.HMAC512(jwtConfig.getSecret())).build().verify(token).getClaim("email").asString();
    }

    /**
     * JWT 토큰 생성
     *
     * @param email 사용자 이메일
     * @return JWT 토큰
     */
    public String createJWTToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(jwtConfig.getSecret()));
    }
}
