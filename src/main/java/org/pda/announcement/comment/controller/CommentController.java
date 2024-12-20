package org.pda.announcement.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pda.announcement.comment.dto.*;
import org.pda.announcement.comment.service.CommentService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.pda.announcement.util.security.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "Comment API", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    @GetMapping("/stock/{stock_id}")
    @Operation(summary = "특정 종목의 모든 공시 중 최신 댓글 조회", description = "특정 종목의 모든 공시 중 최신 댓글 최대 10개를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = StockCommentResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 stock_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> getLatestCommentsByStock(@PathVariable("stock_id") Long stockId,
                                                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                                                      @RequestParam(name = "size", defaultValue = "10") int size) {
        List<StockCommentResponse> comments = commentService.getLatestCommentsByStock(stockId, page, size);
        return ResponseEntity.ok(new ApiCustomResponse("특정 종목 전체 공시 최신 댓글 목록 조회 성공", comments));
    }

    @GetMapping("/announcement/{announcement_id}")
    @Operation(summary = "특정 공시 댓글 목록 조회", description = "특정 공시에 달린 댓글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 announcement_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> getCommentsByAnnouncement(@PathVariable("announcement_id") Long announcementId,
                                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                                       @RequestParam(name = "size", defaultValue = "3") int size) {
        List<AnnouncementCommentResponse> comments = commentService.getCommentsByAnnouncement(announcementId, page, size);
        return ResponseEntity.ok(new ApiCustomResponse("특정 공시 댓글 목록 조회 성공", comments));
    }

    @GetMapping("/my")
    @Operation(summary = "내 댓글 목록 조회", description = "로그인한 사용자가 작성한 댓글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> getMyComments(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "페이지 번호") int page,
            @RequestParam(name = "size", defaultValue = "3") @Parameter(description = "페이지당 댓글 수") int size) {
        String email = jwtService.getUserEmailByJWT(token);
        List<MyCommentResponse> comments = commentService.getMyComments(email, page, size);
        return ResponseEntity.ok(new ApiCustomResponse("내 댓글 목록 조회 성공", comments));
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
                                                           @RequestBody CommentRequest request,
                                                           @RequestHeader("Authorization") String token) {
        // 댓글 작성 로직
        Long cc = commentService.createComment(announcement_id, request.getContent(), jwtService.getUserEmailByJWT(token));
        return ResponseEntity.ok(new ApiCustomResponse("댓글 작성 성공",cc));
    }

    @PatchMapping("/{comment_id}")
    @Operation(summary = "댓글 수정", description = "본인이 작성한 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 comment_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<ApiCustomResponse> updateComment(@PathVariable("comment_id") Long comment_id,
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
    public ResponseEntity<ApiCustomResponse> deleteComment(@PathVariable("comment_id") Long comment_id,
                                                           @RequestHeader("Authorization") String token) {
        // 댓글 삭제 로직
        commentService.deleteComment(comment_id, jwtService.getUserEmailByJWT(token));
        return ResponseEntity.ok(new ApiCustomResponse("댓글 삭제 성공"));
    }
}
