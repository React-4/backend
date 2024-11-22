package org.pda.announcement.favoriteannouncement.controller;

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
    public ResponseEntity<?> addFavoriteAnnouncement(@PathVariable Long announcement_id, @RequestHeader("Authorization") String token) {
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
    public ResponseEntity<?> removeFavoriteAnnouncement(@PathVariable Long announcement_id, @RequestHeader("Authorization") String token) {
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
    public ResponseEntity<?> checkFavoriteAnnouncement(@PathVariable Long announcement_id, @RequestHeader("Authorization") String token) {
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
