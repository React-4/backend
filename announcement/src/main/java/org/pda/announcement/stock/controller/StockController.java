package org.pda.announcement.stock.controller;

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
public class StockController {

    private final StockService stockService;


    // 티커로 주식 찾기 (GET /api/stock/ticker/{ticker})
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
