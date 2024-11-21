package org.pda.announcement.util.security;


import lombok.RequiredArgsConstructor;
import org.pda.announcement.util.security.jwt.JwtRequestFilter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    @Order(1)
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
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    protected SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // http 시큐리티 빌더
        return http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(cf -> cf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // session 을 사용하지 않음
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(FRONT_URL + "/**").permitAll()
                        .requestMatchers("/api/user/signup").permitAll()
                        .requestMatchers("/api/user/login").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("**").permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(authenticationManager -> authenticationManager
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())) // 예외 처리
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilter(corsFilter) // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 등록
                .build();
    }

}
