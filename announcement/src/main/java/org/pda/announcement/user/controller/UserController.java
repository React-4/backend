package org.pda.announcement.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pda.announcement.user.dto.UserSignupRequest;
import org.pda.announcement.user.service.UserService;
import org.pda.announcement.util.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User", description = "회원가입, 로그인, 회원정보 수정, 회원탈퇴")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 요청
     * POST	/api/user/signup	Content-Type: application/json
     * json {"nickname": "string", "password": "string", "email": "string", "birth_date": "YYYY-MM-DD"}
     * - 성공 (201 OK):
     * - 응답 본문: json {"message": "회원가입 성공"}
     * 400 Bad Request: json {"error": "필수 필드 누락"}
     * 409 Conflict: json {"error": "닉네임 또는 이메일 중복"}
     * profile_color는 서버에서 10개의 색상 중 랜덤으로 지정되어 응답에 포함됨. 색상 코드 참조
     * - role은 이후에 개발, 기본으로 guest로 저장
     */
    public ResponseEntity<ApiResponse> signup(@RequestBody UserSignupRequest userSignupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("회원가입 성공",
                        userService.signup(userSignupRequest)));
    }
}
