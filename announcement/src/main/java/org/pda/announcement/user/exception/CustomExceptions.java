package org.pda.announcement.user.exception;

public class CustomExceptions {

    public static class MissingFieldException extends RuntimeException {
        public MissingFieldException(String message) {
            super(message);
        }
    }

    public static class DuplicateFieldException extends RuntimeException {
        public DuplicateFieldException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class CurrentPasswordMismatchException extends RuntimeException {
        public CurrentPasswordMismatchException(String message) {
            super(message);
        }
    }
}
