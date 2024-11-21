package org.pda.announcement.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.user.dto.UserLoginRequest;
import org.pda.announcement.user.dto.UserLoinResponse;
import org.pda.announcement.user.dto.UserSignupRequest;
import org.pda.announcement.user.service.UserService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.pda.announcement.util.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User", description = "회원가입, 로그인, 회원정보 수정, 회원탈퇴")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "사용자 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "필수 필드 누락"),
            @ApiResponse(responseCode = "409", description = "닉네임 또는 이메일 중복",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        userService.signup(userSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiCustomResponse("회원가입 성공"));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 인증을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = UserLoinResponse.class))),
            @ApiResponse(responseCode = "401", description = "잘못된 이메일 또는 비밀번호",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        UserLoinResponse userResponse = userService.login(userLoginRequest);
        String token = jwtService.createJWTToken(userResponse.getEmail());

        // JWT 토큰을 쿠키에 저장
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(new ApiCustomResponse("로그인 성공", userService.login(userLoginRequest)));
    }
}
