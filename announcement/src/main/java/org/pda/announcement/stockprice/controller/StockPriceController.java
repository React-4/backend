package org.pda.announcement.stockprice.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.stockprice.service.StockPriceService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stockprice")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    @GetMapping("/{stock_id}")
    public ResponseEntity<?> getStockPrice(@PathVariable Long stock_id,
                                           @RequestParam String type,
                                           @RequestParam int length) {
        // 유효한 type 값 검증
        if (!type.equals("day") && !type.equals("week") && !type.equals("month")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomResponse("유효하지 않은 타입입니다"));
        }

        // StockPrice 데이터를 조회
        List<?> stockPriceData = stockPriceService.getStockPriceByTypeAndLength(stock_id, type, length);

        if (stockPriceData == null || stockPriceData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomResponse("존재 하지 않는 stock_id입니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ApiCustomResponse("주식 가격 조회 성공", stockPriceData));
    }
}
