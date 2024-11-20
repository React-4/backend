package org.pda.announcement.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundException(UserNotFoundException e) {
        log.error("Error occurs {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> jsonProcessingException(JsonProcessingException e) {
        log.error("Error occurs {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}