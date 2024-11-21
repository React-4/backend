package org.pda.announcement.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.pda.announcement.comment.dto.CommentRequest;
import org.pda.announcement.comment.dto.CommentResponse;
import org.pda.announcement.comment.service.CommentService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.pda.announcement.util.security.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    @GetMapping("/{announcement_id}")
    @Operation(summary = "특정 공시 댓글 목록 조회", description = "특정 공시에 달린 댓글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 announcement_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> getComments(@PathVariable Long announcement_id, @RequestParam int page) {
        // 댓글 목록 조회 로직
        return null;
    }

    @PostMapping("/{announcement_id}")
    @Operation(summary = "댓글 작성", description = "특정 공시에 댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "필수 필드 누락",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> createComment(@PathVariable("announcement_id") Long announcement_id,
                                                           @RequestBody String content,
                                                           @RequestHeader("Authorization") String token) {
        // 댓글 작성 로직
        commentService.createComment(announcement_id, content, jwtService.getUserEmailByJWT(token));
        return ResponseEntity.ok(new ApiCustomResponse("댓글 작성 성공"));
    }

    @PatchMapping("/{comment_id}")
    @Operation(summary = "댓글 수정", description = "본인이 작성한 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 comment_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> updateComment(@PathVariable Long comment_id,
                                                           @RequestBody CommentRequest commentRequest,
                                                           @RequestHeader("Authorization") String token) {
        // 댓글 수정 로직
        commentService.updateComment(comment_id, commentRequest, jwtService.getUserEmailByJWT(token));
        return ResponseEntity.ok(new ApiCustomResponse("댓글 수정 성공"));
    }

    @DeleteMapping("/{comment_id}")
    @Operation(summary = "댓글 삭제", description = "본인이 작성한 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 comment_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> deleteComment(@PathVariable Long comment_id,
                                                           @RequestHeader("Authorization") String token) {
        // 댓글 삭제 로직
        return null;
    }
}
