package org.pda.announcement.stockprice.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.stock.dto.StockSearchResponse;
import org.pda.announcement.stock.service.StockServiceImpl;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final StockServiceImpl stockService;

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

            // 모든 티커를 수집
            List<String> tickers = stockRankMap.values().stream()
                    .map(StockRankResponse::getStockCode) // StockRankResponse에서 티커 가져오기
                    .collect(Collectors.toList());

            List<StockSearchResponse> stockSearchResponses = stockService.getStocksByTickers(tickers);
// 조회 결과를 Map으로 변환 (티커를 키로 사용, 중복 키는 마지막 값을 사용)
            Map<String, StockSearchResponse> stockResponseMap = stockSearchResponses.stream()
                    .collect(Collectors.toMap(
                            StockSearchResponse::getTicker, // 키: 티커
                            response -> response,           // 값: StockSearchResponse
                            (existing, replacement) -> replacement // 중복 키 발생 시 새로운 값으로 대체
                    ));


            // stockRankMap에 ID와 종목명 추가
            for (StockRankResponse stockRankResponse : stockRankMap.values()) {
                String ticker = stockRankResponse.getStockCode();
                StockSearchResponse stockSearchResponse = stockResponseMap.get(ticker);

                if (stockSearchResponse != null) {
                    stockRankResponse.setStockId(stockSearchResponse.getStockId());
                    stockRankResponse.setStockName(stockSearchResponse.getCompanyName());
                }
            }

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
                    parseDouble((String) stockData.get("changeAmount")), // Double로 변환
                    parseDouble((String) stockData.get("accTradeVolume")), // Double로 변환
                    parseLong((String) stockData.get("marketCap")), // Long으로 변환
                    parseDouble((String) stockData.get("foreignRatio")) // Double로 변환
            );
        } else {
            return null; // 데이터가 없는 경우
        }
    }
    @Override
    public Map<String, StockCurrentPriceResponse> getStockCurrentPrices(List<String> tickers) {
        // Redis에서 여러 티커의 데이터를 한 번에 가져옴
        Map<String, Map<Object, Object>> redisData = redisService.getStockCurrentPricesByTickers(tickers);

        // 데이터를 DTO로 변환
        return redisData.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty()) // 유효한 데이터만 필터링
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // 키는 티커
                        entry -> {
                            Map<Object, Object> stockData = entry.getValue();
                            return new StockCurrentPriceResponse(
                                    (String) stockData.get("ticker"),
                                    (String) stockData.get("name"),
                                    Double.valueOf((String) stockData.get("currentPrice")),
                                    Double.valueOf((String) stockData.get("changeRate")),
                                    Double.valueOf((String) stockData.get("changeAmount")),
                                    Double.valueOf((String) stockData.get("accTradeVolume")),
                                    Long.valueOf((String) stockData.get("marketCap")),
                                    Double.valueOf((String) stockData.get("foreignRatio"))
                            );
                        }
                ));
    }

    @Override
    public Map<String, StockCurrentPriceResponse> getAllStockCurrentPrices() {
        // Redis에서 여러 티커의 데이터를 한 번에 가져옴
        Map<String, Map<Object, Object>> redisData = redisService.getAllStockCurrentPrices();

        // 데이터를 DTO로 변환
        return redisData.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty()) // 유효한 데이터만 필터링
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // 키는 티커
                        entry -> {
                            Map<Object, Object> stockData = entry.getValue();
                            return new StockCurrentPriceResponse(
                                    (String) stockData.get("ticker"),
                                    (String) stockData.get("name"),
                                    Double.valueOf((String) stockData.get("currentPrice")),
                                    Double.valueOf((String) stockData.get("changeRate")),
                                    Double.valueOf((String) stockData.get("changeAmount")),
                                    Double.valueOf((String) stockData.get("accTradeVolume")),
                                    Long.valueOf((String) stockData.get("marketCap")),
                                    Double.valueOf((String) stockData.get("foreignRatio"))
                            );
                        }
                ));
    }


    @Override
    public Map<Integer, StockRankResponse> getFavStockPriceFromRedis(List<Long> stockIds) {
        try {
            // Stock ID로 Stock 정보를 가져오기
            List<StockSearchResponse> stockSearchResponses = stockService.getStocksByIds(stockIds);

            // 모든 티커 수집
            List<String> tickers = stockSearchResponses.stream()
                    .map(StockSearchResponse::getTicker)
                    .collect(Collectors.toList());

            // Redis에서 티커별 현재가 데이터 가져오기
            Map<String, Map<Object, Object>> redisPriceData = redisService.getStockCurrentPricesByTickers(tickers);

            // StockRankResponse로 매핑할 Map 생성
            Map<Integer, StockRankResponse> stockRankMap = new HashMap<>();

            // stocks 데이터를 순회하며 StockRankResponse 생성 및 매핑
            for (StockSearchResponse stock : stockSearchResponses) {
                StockRankResponse stockRankResponse = new StockRankResponse();
                stockRankResponse.setStockId(stock.getStockId());
                stockRankResponse.setStockName(stock.getCompanyName());
                stockRankResponse.setStockCode(stock.getTicker());

                // Redis 데이터에서 가격 정보 추가
                Map<Object, Object> priceData = redisPriceData.get(stock.getTicker());
                System.out.println(priceData);
                if (priceData != null) {
                    stockRankResponse.setCurrentPrice(priceData.get("currentPrice") != null
                            ? String.valueOf(priceData.get("currentPrice"))
                            : "0");
                    stockRankResponse.setChangeRate(priceData.get("changeRate") != null
                            ? String.valueOf(priceData.get("changeRate"))
                            : "0");
                    stockRankResponse.setVolume(priceData.get("accTradeVolume") != null
                            ? String.valueOf(priceData.get("accTradeVolume"))
                            : "0");
                }

                // ID를 키로 추가
                stockRankMap.put(stockRankResponse.getStockId().intValue(), stockRankResponse);
            }

            // 최종 Map 반환
            return stockRankMap;

        } catch (Exception e) {
            // 예외 처리 (로그를 출력하고 빈 Map 반환)
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

}