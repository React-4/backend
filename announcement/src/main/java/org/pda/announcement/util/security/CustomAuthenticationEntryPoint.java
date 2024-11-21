package org.pda.announcement.util.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pda.announcement.util.security.jwt.JwtErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object exceptionObj = request.getAttribute("exception");
        String errorMessage = "인증 에러가 발생했습니다."; // 기본 에러 메시지

        if (exceptionObj instanceof JwtErrorCode jwtErrorCode) {
            setResponse(response, jwtErrorCode.getCode() + " : " + jwtErrorCode.getMessage());
        } else {
            setResponse(response, errorMessage);
        }
    }

    private void setResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter()
                .println("{\"errorMessage\": \"" + errorMessage + "\"}");
    }
}
