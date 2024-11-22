package org.pda.announcement.favoritestock.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.favoritestock.service.FavoriteStockService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.pda.announcement.util.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite/stock")
public class FavoriteStockController {

    private final FavoriteStockService favoriteStockService;
    private final JwtService jwtService;

    // 관심 종목 추가
    @PostMapping("/{stock_id}")
    public ResponseEntity<?> addFavoriteStock(@PathVariable Long stock_id, @RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.getUserEmailByJWT(token); // JWT에서 사용자 이메일 추출
            favoriteStockService.addFavoriteStock(stock_id, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiCustomResponse("관심 종목 추가 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorCustomResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorCustomResponse("이미 관심 종목에 추가됨"));
        }
    }

    // 관심 종목 삭제
    @DeleteMapping("/{stock_id}")
    public ResponseEntity<?> removeFavoriteStock(@PathVariable Long stock_id, @RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.getUserEmailByJWT(token); // JWT에서 사용자 이메일 추출
            favoriteStockService.removeFavoriteStock(stock_id, userEmail);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiCustomResponse("관심 종목 삭제 성공"));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("유효하지 않은 stock_id")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorCustomResponse("유효하지 않은 stock_id"));
            } else if (e.getMessage().equals("관심 종목이 눌리지 않았습니다")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorCustomResponse("관심 종목이 눌리지 않았습니다"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorCustomResponse("삭제 실패"));
            }
        }
    }

    // 관심 종목 목록 조회
    @GetMapping
    public ResponseEntity<?> getFavoriteStockList(@RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.getUserEmailByJWT(token); // JWT에서 사용자 이메일 추출
            return ResponseEntity.status(HttpStatus.OK)
                    .body(favoriteStockService.getFavoriteStockList(userEmail));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorCustomResponse("인증이 필요합니다"));
        }
    }

    // 관심 종목이 이미 추가되었는지 확인
    @GetMapping("/{stock_id}/check")
    public ResponseEntity<?> checkFavoriteStock(@PathVariable Long stock_id, @RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.getUserEmailByJWT(token); // JWT에서 사용자 이메일 추출
            boolean isFavorite = favoriteStockService.isFavoriteStock(stock_id, userEmail);
            if (isFavorite) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiCustomResponse("관심 종목에 이미 추가됨",1));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiCustomResponse("관심 종목에 추가되지 않음",0));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorCustomResponse("인증이 필요합니다"));
        }
    }
}
