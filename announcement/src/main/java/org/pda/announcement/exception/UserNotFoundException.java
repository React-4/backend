package org.pda.announcement.exception;

public class UserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "회원 정보를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}