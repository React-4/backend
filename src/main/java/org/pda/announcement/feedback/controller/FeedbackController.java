package org.pda.announcement.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.feedback.domain.FeedbackType;
import org.pda.announcement.feedback.dto.FeedbackRequest;
import org.pda.announcement.feedback.service.FeedbackService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    // 특정 공시 투표 조회
    @GetMapping("/{announcement_id}")
    public ResponseEntity<?> getFeedback(@PathVariable Long announcement_id) {
        try {
            var feedbackData = feedbackService.getFeedback(announcement_id);
            return ResponseEntity.status(HttpStatus.OK).body(feedbackData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorCustomResponse("유효하지 않은 announcement_id"));
        }
    }

    // 특정 공시 투표 추가
    @PostMapping("/{announcement_id}")
    public ResponseEntity<?> addFeedback(@PathVariable Long announcement_id,
                                         @RequestHeader("Authorization") String token,
                                         @RequestBody FeedbackType feedbackRequest) {
        try {
            var result = feedbackService.addFeedback(announcement_id, token, feedbackRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorCustomResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorCustomResponse("이미 투표한 상태입니다"));
        }
    }

    // 특정 공시 투표 삭제
    @DeleteMapping("/{announcement_id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long announcement_id, @RequestHeader("Authorization") String token) {
        try {
            feedbackService.deleteFeedback(announcement_id, token);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiCustomResponse("투표 삭제 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorCustomResponse("유효하지 않은 announcement_id"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorCustomResponse("삭제할 투표가 없습니다"));
        }
    }
}
