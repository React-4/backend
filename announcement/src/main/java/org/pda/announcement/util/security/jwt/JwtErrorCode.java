package org.pda.announcement.util.security.jwt;

import lombok.Getter;

/**
 * Jwt 에러 코드
 */
@Getter
public enum JwtErrorCode {
    NOTFOUND_TOKEN(400, "Authorization이 없습니다."),
    UNSUPPORTED_TOKEN(400, "Bearer로 시작하지 않습니다."),
    EXPIRED_TOKEN(400, "토큰이 만료되었습니다."),
    WRONG_TYPE_TOKEN(400, "유효하지 않은 토큰입니다.");

    private int code;
    private String message;

    JwtErrorCode(int status, String message) {
        this.code = status;
        this.message = message;
    }
}
