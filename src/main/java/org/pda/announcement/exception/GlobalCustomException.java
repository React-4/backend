package org.pda.announcement.exception;

public class GlobalCustomException {
    public static class CommentNotFoundException extends RuntimeException {
        public CommentNotFoundException() {
            super("유효하지 않은 comment_id입니다.");
        }

        public CommentNotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicateFieldException extends RuntimeException {
        public DuplicateFieldException() {
            super("이미 존재하는 값입니다. 다른 값을 입력해주십시오.");
        }

        public DuplicateFieldException(String message) {
            super(message);
        }
    }

    public static class AnnouncementNotFoundException extends RuntimeException {
        public AnnouncementNotFoundException() {
            super("유효하지 않은 announcement_id입니다.");
        }

        public AnnouncementNotFoundException(String message) {
            super(message);
        }
    }

    public static class StockNotFoundException extends RuntimeException {
        public StockNotFoundException() {
            super("유효하지 않은 stock_id입니다.");
        }

        public StockNotFoundException(String message) {
            super(message);
        }
    }

    public static class MissingFieldException extends RuntimeException {
        public MissingFieldException() {
            super("필수 입력 값이 누락되었습니다.");
        }

        public MissingFieldException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException() {
            super("인증되지 않은 사용자입니다.");
        }

        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException() {
            super("유효하지 않은 이메일 또는 비밀번호입니다.");
        }

        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super("유효하지 않은 사용자입니다.");
        }

        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class IllegalArgumentException extends RuntimeException {
        public IllegalArgumentException() {
            super("유효하지 않은 정렬 기준입니다");
        }

        public IllegalArgumentException(String message) {
            super(message);
        }
    }

    public static class CurrentPasswordMismatchException extends RuntimeException {
        public CurrentPasswordMismatchException() {
            super("현재 비밀번호가 일치하지 않습니다.");
        }

        public CurrentPasswordMismatchException(String message) {
            super(message);
        }
    }
}
