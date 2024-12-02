package org.pda.announcement.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.pda.announcement.feedback.domain.FeedbackType;
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
    @Operation(summary = "공시 투표 조회", description = "특정 공시에 대한 투표 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공시 투표 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class)))
    })
    public ResponseEntity<?> getFeedback(@Parameter(description = "공시 id") @PathVariable Long announcement_id) {
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
    @Operation(summary = "공시 투표", description = "특정 공시에 대한 투표를 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공시 투표 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class)))
    })
    public ResponseEntity<?> addFeedback(@Parameter(description = "공시 id") @PathVariable Long announcement_id,
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
    @Operation(summary = "공시 투표 삭제", description = "특정 공시에 대한 투표 삭제를 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공시 투표 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class)))
    })
    public ResponseEntity<?> deleteFeedback(@Parameter(description = "공시 id") @PathVariable Long announcement_id, @RequestHeader("Authorization") String token) {
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
