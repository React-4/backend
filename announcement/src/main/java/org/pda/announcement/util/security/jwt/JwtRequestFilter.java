package org.pda.announcement.util.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.pda.announcement.util.security.jwt.JwtErrorCode.*;

/**
 * JWT 토큰 검증을 위한 필터
 */
@Slf4j
@Component
@Profile({"local", "server"})
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    // JWT 토큰 접두어
    public static final String TOKEN_PREFIX = "Bearer ";
    // JWT 헤더
    public static final String HEADER_STRING = "Authorization";
    // 제외 URL
    private static final List<String> EXCLUDE_URL = List.of(
            "/favicon.ico",
            "/swagger/**",
            "/swagger-resources/**",
            "/swagger-ui/**", "/webjars/**", "/swagger-ui.html",
            "/v3/api-docs/**",
            "/api/user/signup",
            "/api/user/login"
    );
    private final JwtConfig jwtConfig;

    /**
     * JWT 토큰 검증을 위한 필터
     *
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @param filterChain FilterChain
     * @throws ServletException 예외
     * @throws IOException      예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String jwtHeader = request.getHeader(HEADER_STRING);
            // 제외 URL
            if (pathMatchesExcludePattern(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
            // JWT 헤더가 없을 경우
            if (jwtHeader == null || jwtHeader.isEmpty()) {
                request.setAttribute("exception", NOTFOUND_TOKEN);
                throw new JwtFilterException(NOTFOUND_TOKEN);
            }
            // JWT 토큰 접두어가 없을 경우
            if (!jwtHeader.startsWith(TOKEN_PREFIX)) {
                request.setAttribute("exception", UNSUPPORTED_TOKEN);
                throw new JwtFilterException(UNSUPPORTED_TOKEN);
            }

            String token = jwtHeader.replace(TOKEN_PREFIX, "");
            request.setAttribute("email", JWT.require(Algorithm.HMAC512(jwtConfig.getSecret())).build().verify(token).getClaim("email").asString());
        } catch (TokenExpiredException e) {
            request.setAttribute("exception", EXPIRED_TOKEN);
            throw new JwtFilterException(EXPIRED_TOKEN);
        } catch (JWTVerificationException e) {
            request.setAttribute("exception", WRONG_TYPE_TOKEN);
            throw new JwtFilterException(WRONG_TYPE_TOKEN);
        } catch (JwtFilterException e) {
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean pathMatchesExcludePattern(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String excludeUrl : EXCLUDE_URL) {
            if (pathMatcher.match(excludeUrl, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
