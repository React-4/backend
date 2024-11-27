package org.pda.announcement.favoritestock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "관심 종목 추가", description = "특정 주식을 관심 종목에 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "관심 종목 추가 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 stock_id",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 관심 종목에 추가됨",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<?> addFavoriteStock(@Parameter(description = "주식 ID", required = true) @PathVariable("stock_id") Long stock_id, @RequestHeader("Authorization") String token) {
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
    @Operation(summary = "관심 종목 삭제", description = "특정 주식을 관심 종목에서 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심 종목 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 stock_id 또는 관심 종목이 눌리지 않았습니다",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<?> removeFavoriteStock(@Parameter(description = "주식 ID", required = true) @PathVariable("stock_id") Long stock_id, @RequestHeader("Authorization") String token) {
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
    @Operation(summary = "관심 종목 목록 조회", description = "사용자의 관심 종목 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심 종목 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증이 필요함",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
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
    @Operation(summary = "관심 종목 확인", description = "특정 주식이 관심 종목에 추가되었는지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심 종목 확인 성공",
                    content = @Content(schema = @Schema(implementation = ApiCustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증이 필요함",
                    content = @Content(schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    public ResponseEntity<?> checkFavoriteStock(@Parameter(description = "주식 ID", required = true) @PathVariable("stock_id") Long stock_id, @RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.getUserEmailByJWT(token); // JWT에서 사용자 이메일 추출
            boolean isFavorite = favoriteStockService.isFavoriteStock(stock_id, userEmail);
            if (isFavorite) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiCustomResponse("관심 종목에 이미 추가됨", 1));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiCustomResponse("관심 종목에 추가되지 않음", 0));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorCustomResponse("인증이 필요합니다"));
        }
    }
}
