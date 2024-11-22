package org.pda.announcement.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.exception.GlobalCustomException.*;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 필수 필드 누락 처리
    @ExceptionHandler(MissingFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCustomResponse> handleMissingField(MissingFieldException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomResponse(ex.getMessage()));
    }

    // 닉네임 또는 이메일 중복 처리
    @ExceptionHandler(DuplicateFieldException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorCustomResponse> handleDuplicateField(DuplicateFieldException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorCustomResponse(ex.getMessage()));
    }

    // 잘못된 이메일 또는 비밀번호 처리
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorCustomResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCustomResponse(ex.getMessage()));
    }

    // 유효하지 않은 user_id 처리
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorCustomResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorCustomResponse(ex.getMessage()));
    }

    // 현재 비밀번호 불일치 처리
    @ExceptionHandler(CurrentPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorCustomResponse> handleCurrentPasswordMismatch(CurrentPasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCustomResponse(ex.getMessage()));
    }

    // JsonProcessingException 처리
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> jsonProcessingException(JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // 유효하지 않은 announcement_id 처리
    @ExceptionHandler(AnnouncementNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCustomResponse> handleAnnouncementNotFound(AnnouncementNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomResponse(ex.getMessage()));
    }

    // 기타 예외 처리 (예: 유효하지 않은 요청)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCustomResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomResponse(ex.getMessage()));
    }
}
