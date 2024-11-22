package org.pda.announcement.stock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pda.announcement.stock.dto.StockAutocompleteResponse;
import org.pda.announcement.stock.dto.StockSearchResponse;
import org.pda.announcement.stock.service.StockService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock")
@Tag(name = "Stock", description = "주식 관련 API")
public class StockController {

    private final StockService stockService;


    // 티커로 주식 찾기 (GET /api/stock/ticker/{ticker})
    @Operation(summary = "티커로 주식 조회", description = "주어진 티커로 주식 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주식 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "주식 정보가 존재하지 않음")
    })
    @GetMapping("/ticker/{ticker}")
    public ResponseEntity<?> getStockByTicker(@PathVariable String ticker) {
        Optional<StockSearchResponse> stock = stockService.getStockByTicker(ticker);

        if (stock.isEmpty()) {
            // 오류 응답: 주식이 없을 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorCustomResponse("해당 티커의 주식이 존재하지 않습니다"));
        }

        // 성공적인 응답: 주식 정보 반환
        return ResponseEntity.status(HttpStatus.OK).body(new ApiCustomResponse("주식 정보 조회 성공", stock.get()));
    }

    // 주식 검색 (GET /api/stock/search)
    @Operation(summary = "주식 검색", description = "검색어로 주식을 검색합니다. 티커 또는 회사명으로 자동완성 기능을 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공"),
            @ApiResponse(responseCode = "400", description = "검색어가 비어있음"),
            @ApiResponse(responseCode = "404", description = "검색 결과가 없음")
    })
    @GetMapping("/search")
    public ResponseEntity<ApiCustomResponse> searchStocks(@RequestParam String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            // 오류 응답: 검색어가 비어있을 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiCustomResponse("검색어를 입력해주세요", null));
        }

        List<StockAutocompleteResponse> stocks = stockService.searchStocks(keyword);

        if (stocks.isEmpty()) {
            // 오류 응답: 검색 결과가 없을 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiCustomResponse("검색 결과가 없습니다", null));
        }

        // 성공적인 응답: 검색된 주식들 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiCustomResponse("검색 성공", stocks));
    }
}
