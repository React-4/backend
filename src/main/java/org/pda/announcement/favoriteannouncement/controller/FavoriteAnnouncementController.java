package org.pda.announcement.favoriteannouncement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.pda.announcement.favoriteannouncement.service.FavoriteAnnouncementService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.pda.announcement.util.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite/announcement")
public class FavoriteAnnouncementController {

    private final FavoriteAnnouncementService favoriteAnnouncementService;
    private final JwtService jwtService;

    // 관심 공시 추가
    @PostMapping("/{announcement_id}")
    @Operation(summary = "관심 공시 추가", description = "특정 공시를 관심 공시에 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "관심 공시 추가 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 announcement_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 관심 공시에 추가됨",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<?> addFavoriteAnnouncement(@Parameter(description = "공시 ID", required = true) @PathVariable(name = "announcement_id") Long announcement_id, @RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.getUserEmailByJWT(token); // JWT에서 사용자 이메일 추출
            favoriteAnnouncementService.addFavoriteAnnouncement(announcement_id, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiCustomResponse("관심 공시 추가 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorCustomResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorCustomResponse("이미 관심 공시에 추가됨"));
        }
    }

    // 관심 공시 삭제
    @DeleteMapping("/{announcement_id}")
    @Operation(summary = "관심 공시 삭제", description = "특정 공시를 관심 공시에서 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심 공시 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 announcement_id 또는 관심 공시가 눌리지 않았습니다",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<?> removeFavoriteAnnouncement(@Parameter(description = "공시 ID", required = true) @PathVariable(name = "announcement_id") Long announcement_id, @RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.getUserEmailByJWT(token); // JWT에서 사용자 이메일 추출
            favoriteAnnouncementService.removeFavoriteAnnouncement(announcement_id, userEmail);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiCustomResponse("관심 공시 삭제 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorCustomResponse(e.getMessage()));
        }
    }

    // 관심 공시 목록 조회
    @GetMapping
    @Operation(summary = "관심 공시 목록 조회", description = "사용자의 관심 공시 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심 공시 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class)))
    })
    public ResponseEntity<?> getFavoriteAnnouncementList(@RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.getUserEmailByJWT(token); // JWT에서 사용자 이메일 추출
            return ResponseEntity.status(HttpStatus.OK)
                    .body(favoriteAnnouncementService.getFavoriteAnnouncementList(userEmail));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorCustomResponse("인증이 필요합니다"));
        }
    }

    // 관심 공시가 이미 추가되었는지 확인
    @GetMapping("/{announcement_id}/check")
    @Operation(summary = "관심 공시 확인", description = "특정 공시가 이미 관심 공시에 추가되었는지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심 공시 확인 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class)))
    })
    public ResponseEntity<?> checkFavoriteAnnouncement(@Parameter(description = "공시 ID", required = true) @PathVariable(name = "announcement_id") Long announcement_id, @RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.getUserEmailByJWT(token); // JWT에서 사용자 이메일 추출
            boolean isFavorite = favoriteAnnouncementService.isFavoriteAnnouncement(announcement_id, userEmail);
            if (isFavorite) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiCustomResponse("관심 공시 이미 추가됨", 1));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiCustomResponse("관심 공시 추가되지 않음", 0));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorCustomResponse("인증이 필요합니다"));
        }
    }
}
