package org.pda.announcement.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.user.dto.*;
import org.pda.announcement.user.service.UserService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.pda.announcement.util.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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

    @PatchMapping
    @Operation(summary = "닉네임 수정", description = "사용자의 닉네임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 수정 성공",
                    content = @Content(schema = @Schema(implementation = UpdateNicknameResponse.class))),
            @ApiResponse(responseCode = "400", description = "필수 필드 누락",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 user_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> updateNickname(@Valid @RequestBody UpdateNicknameRequest request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ApiCustomResponse("닉네임 수정 성공", userService.updateNickname(request, jwtService.getUserEmailByJWT(token))));
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "필수 필드 누락",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "현재 비밀번호가 일치하지 않습니다",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request, @RequestHeader("Authorization") String token) {
        userService.updatePassword(request, jwtService.getUserEmailByJWT(token));
        return ResponseEntity.ok(new ApiCustomResponse("비밀번호 변경 성공"));
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "사용자의 계정을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "필수 필드 누락",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 email",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않습니다",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> deleteUser(@Valid @RequestBody DeleteUserRequest request, @RequestHeader("Authorization") String token) {
        userService.deleteUser(request, jwtService.getUserEmailByJWT(token));
        return ResponseEntity.ok(new ApiCustomResponse("회원 탈퇴 성공"));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> logout(HttpServletResponse response) {
        // JWT 토큰을 만료시키기 위해 쿠키의 max age를 0으로 설정
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 만료
        response.addCookie(cookie);

        return ResponseEntity.ok(new ApiCustomResponse("로그아웃 성공"));
    }
}
