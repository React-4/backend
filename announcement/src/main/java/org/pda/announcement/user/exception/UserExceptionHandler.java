package org.pda.announcement.user.exception;

import org.pda.announcement.exception.UserNotFoundException;
import org.pda.announcement.user.exception.CustomExceptions.CurrentPasswordMismatchException;
import org.pda.announcement.user.exception.CustomExceptions.DuplicateFieldException;
import org.pda.announcement.user.exception.CustomExceptions.MissingFieldException;
import org.pda.announcement.user.exception.CustomExceptions.UnauthorizedException;
import org.pda.announcement.util.api.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    // 필수 필드 누락 처리
    @ExceptionHandler(MissingFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMissingField(MissingFieldException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("필수 필드 누락"));
    }

    // 닉네임 또는 이메일 중복 처리
    @ExceptionHandler(DuplicateFieldException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleDuplicateField(DuplicateFieldException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("닉네임 또는 이메일 중복"));
    }

    // 잘못된 이메일 또는 비밀번호 처리
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("잘못된 이메일 또는 비밀번호"));
    }

    // 유효하지 않은 user_id 처리
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("유효하지 않은 user_id"));
    }

    // 현재 비밀번호 불일치 처리
    @ExceptionHandler(CurrentPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleCurrentPasswordMismatch(CurrentPasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("현재 비밀번호가 일치하지 않습니다"));
    }

    // 기타 예외 처리 (예: 유효하지 않은 요청)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("잘못된 요청입니다"));
    }
}
