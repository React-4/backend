package org.pda.announcement.stockprice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.stockprice.dto.StockCurrentPriceResponse;
import org.pda.announcement.stockprice.dto.StockRankResponse;
import org.pda.announcement.stockprice.repository.StockPriceDayRepository;
import org.pda.announcement.stockprice.repository.StockPriceMonthRepository;
import org.pda.announcement.stockprice.repository.StockPriceWeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockPriceServiceImpl implements StockPriceService {

    private final StockPriceDayRepository stockPriceDayRepository;
    private final StockPriceMonthRepository stockPriceMonthRepository;
    private final StockPriceWeekRepository stockPriceWeekRepository;

    private final RedisService redisService;  // RedisService 주입

    // type에 따라 가격 데이터를 조회하는 메서드
    public List<?> getStockPriceByTypeAndLength(Long stockId, String type, int length) {
        // Pageable 객체 생성 (첫 번째 페이지, length만큼 데이터 조회)
        Pageable pageable = PageRequest.of(0, length);

        switch (type) {
            case "day":
                return stockPriceDayRepository.findByStockIdOrderByDateDesc(stockId, pageable);
            case "week":
                return stockPriceWeekRepository.findByStockIdOrderByDateDesc(stockId, pageable);
            case "month":
                return stockPriceMonthRepository.findByStockIdOrderByDateDesc(stockId, pageable);
            default:
                return null;
        }
    }

    @Override
    public Map<Integer, StockRankResponse> getStockRankFromRedis(String sortBy) {
        String rankedDataJson = redisService.getStockRankFromRedis(sortBy);

        try {
            // ObjectMapper 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 데이터를 Map<Integer, StockRankResponse>로 변환
            Map<Integer, StockRankResponse> stockRankMap = objectMapper.readValue(
                    rankedDataJson,
                    objectMapper.getTypeFactory().constructMapType(Map.class, Integer.class, StockRankResponse.class)
            );

            // 변환된 Map 반환
            return stockRankMap;

        } catch (Exception e) {
            // 예외 처리 (로그를 출력하고 null 반환)
            e.printStackTrace();
            return null;  // 예외가 발생한 경우 null을 반환
        }
    }

    @Override
    public StockCurrentPriceResponse getStockCurrentPrice(String ticker) {
        // Redis에서 주식 정보를 가져옴
        Map<Object, Object> stockData = redisService.getStockCurrentPriceByTicker(ticker);
        if (stockData != null && !stockData.isEmpty()) {
            // Redis에서 가져온 데이터를 DTO로 변환
            return new StockCurrentPriceResponse(
                    (String) stockData.get("ticker"),
                    (String) stockData.get("name"),
                    parseDouble((String) stockData.get("currentPrice")), // Double로 변환
                    parseDouble((String) stockData.get("changeRate")), // Double로 변환
                    parseDouble((String) stockData.get("accTradeVolume")), // Double로 변환
                    parseDouble((String) stockData.get("changeAmount")), // Double로 변환
                    parseLong((String) stockData.get("marketCap")), // Long으로 변환
                    parseDouble((String) stockData.get("foreignRatio")) // Double로 변환
            );
        } else {
            return null; // 데이터가 없는 경우
        }
    }
}