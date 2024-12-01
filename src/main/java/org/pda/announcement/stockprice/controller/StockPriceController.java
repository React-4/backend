package org.pda.announcement.stockprice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pda.announcement.stockprice.dto.StockCurrentPriceResponse;
import org.pda.announcement.stockprice.dto.StockRankResponse;
import org.pda.announcement.stockprice.service.StockPriceService;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.api.ErrorCustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stockprice")
@Tag(name = "Stock Price", description = "주식 가격 관련 API")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    @Operation(summary = "주식 가격 조회", description = "주식의 가격 정보를 조회합니다. 'day', 'week', 'month' 타입을 지원합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주식 가격 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청, 유효하지 않은 타입 또는 잘못된 stock_id"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 stock_id")
    })
    @GetMapping("/{stock_id}")
    public ResponseEntity<?> getStockPrice(@Parameter(description = "주식 id", required = true) @PathVariable("stock_id") Long stock_id,
                                           @Parameter(description = "정렬 기준", schema = @Schema(allowableValues = {"day", "week", "month"}, defaultValue = "day")) @RequestParam("type") String type,
                                           @Parameter(description = "길이", schema = @Schema(defaultValue = "10")) @RequestParam("length") int length) {
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

    @Operation(summary = "주식 순위 조회", description = "Redis에서 주식 순위를 조회합니다. 거래대금, 거래량, 상승률, 하락률 순위를 지원합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주식 순위 조회 성공"),
            @ApiResponse(responseCode = "400", description = "sort_by 파라미터가 비어있음"),
            @ApiResponse(responseCode = "404", description = "검색 결과가 없음")
    })
    @GetMapping("/rank")
    public ResponseEntity<?> getStockRank(
            @Parameter(description = "정렬 기준", schema = @Schema(allowableValues = {"amount", "volume", "change_rate_up", "change_rate_down"}))
            @RequestParam("sort_by") String sort_by) {
        if (sort_by == null || sort_by.isEmpty()) {
            // 오류 응답: sort_by 파라미터가 비어있을 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorCustomResponse("sort_by 파라미터를 입력해주세요"));
        }

        Map<Integer, StockRankResponse> rankedData = stockPriceService.getStockRankFromRedis(sort_by);

        if (rankedData == null || rankedData.isEmpty()) {
            // 오류 응답: Redis에 데이터가 없을 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorCustomResponse("검색 결과가 없습니다"));
        }

        System.out.println(rankedData);

        // 성공적인 응답: Redis에서 가져온 순위 데이터 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiCustomResponse("주식 순위 조회 성공", rankedData));
    }

    @Operation(summary = "현재 주식 가격 조회", description = "주식의 현재 가격을 조회합니다. 티커를 기반으로 현재 가격 정보를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주식 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "주식 정보가 없음")
    })
    @GetMapping("/current/{ticker}")
    public ResponseEntity<?> getStockCurrentPrice(@Parameter(description = "현재가 조회") @PathVariable("ticker") String ticker) {
        StockCurrentPriceResponse cp = stockPriceService.getStockCurrentPrice(ticker);

        if (cp == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorCustomResponse("주식 정보가 없습니다"));
        }

        return ResponseEntity.status(200)
                .body(new ApiCustomResponse("주식 정보 조회 성공", cp));
    }
}
