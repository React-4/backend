package org.pda.announcement.util.security;


import lombok.RequiredArgsConstructor;
import org.pda.announcement.util.security.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String FRONT_URL = "http://localhost:5173";
    private final CorsFilter corsFilter;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    /**
     * SecurityFilterChain
     * <p>
     * WebSecurityConfigurerAdapter 가 deprecated 되면서 SecurityFilterChain 을 Bean 으로 등록하여 사용
     * </p>
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(FRONT_URL + "/main/**").permitAll()  // 특정 경로는 인증 없이 접근 허용
//                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
//                )
//                .exceptionHandling(exception ->
//                        exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint())  // 사용자 정의 인증 실패 처리
//                )
//                .addFilter(corsFilter)  // CORS 필터 추가
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);  // JWT 필터 등록

        return http.build();
    }
}
