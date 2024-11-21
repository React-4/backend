package org.pda.announcement.user.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.user.exception.CustomExceptions.CurrentPasswordMismatchException;
import org.pda.announcement.user.exception.CustomExceptions.DuplicateFieldException;
import org.pda.announcement.user.exception.CustomExceptions.MissingFieldException;
import org.pda.announcement.user.exception.CustomExceptions.UnauthorizedException;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.pda.announcement.user.exception.CustomExceptions.UserNotFoundException;

@Slf4j
@RestControllerAdvice("org.pda.announcement.user")
public class UserExceptionHandler {

    // 필수 필드 누락 처리
    @ExceptionHandler(MissingFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCustomResponse> handleMissingField(MissingFieldException ex) {
        log.info("필수 필드 누락: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomResponse(ex.getMessage()));
    }

    // 닉네임 또는 이메일 중복 처리
    @ExceptionHandler(DuplicateFieldException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorCustomResponse> handleDuplicateField(DuplicateFieldException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorCustomResponse("닉네임 또는 이메일 중복"));
    }

    // 잘못된 이메일 또는 비밀번호 처리
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorCustomResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCustomResponse("잘못된 이메일 또는 비밀번호"));
    }

    // 유효하지 않은 user_id 처리
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorCustomResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorCustomResponse("유효하지 않은 user_id"));
    }

    // 현재 비밀번호 불일치 처리
    @ExceptionHandler(CurrentPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorCustomResponse> handleCurrentPasswordMismatch(CurrentPasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCustomResponse("현재 비밀번호가 일치하지 않습니다"));
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> jsonProcessingException(JsonProcessingException e) {
        log.error("Error occurs {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // 기타 예외 처리 (예: 유효하지 않은 요청)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCustomResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomResponse(ex.getMessage()));
    }
}
