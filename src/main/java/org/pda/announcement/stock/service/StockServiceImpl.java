package org.pda.announcement.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.stock.domain.Stock;
import org.pda.announcement.stock.dto.StockAutocompleteResponse;
import org.pda.announcement.stock.dto.StockSearchResponse;
import org.pda.announcement.stock.repository.StockRepository;
import org.pda.announcement.stockprice.service.RedisService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final RedisService redisService;

    @Override
    public Optional<StockSearchResponse> getStockByTicker(String ticker) {
        Optional<Stock> stock = stockRepository.findByTicker(ticker);
        return stock.map(StockSearchResponse::fromStock); // Stock 엔티티를 DTO로 변환하여 반환
    }

    @Override
    public List<StockAutocompleteResponse> searchStocks(String keyword) {
        // 1. StockRepository로 주식 검색
        List<Stock> stocks = stockRepository.findByTickerContainingOrCompanyNameContaining(keyword, keyword);

        // 2. Redis에서 현재가, 등락률, 거래량 조회
        List<String> tickers = stocks.stream().map(Stock::getTicker).collect(Collectors.toList());
        Map<String, Map<Object, Object>> stockDataMap = redisService.getStockCurrentPricesByTickers(tickers);

        // 3. 데이터를 DTO로 변환
        return stocks.stream()
                .map(stock -> {
                    Map<Object, Object> stockData = stockDataMap.get(stock.getTicker());
                    Double currentPrice = stockData != null ? Double.valueOf((String) stockData.getOrDefault("currentPrice", "0")) : 0.0;
                    Double changeRate = stockData != null ? Double.valueOf((String) stockData.getOrDefault("changeRate", "0")) : 0.0;
                    Long volume = stockData != null ? Long.valueOf((String) stockData.getOrDefault("volume", "0")) : 0L;

                    return StockAutocompleteResponse.fromStock(stock, currentPrice, changeRate, volume);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StockSearchResponse> getStocksByTickers(List<String> tickers) {
        List<Stock> stocks = stockRepository.findAllByTickerIn(tickers); // Repository 메서드 호출
        return stocks.stream()
                .map(StockSearchResponse::fromStock) // Stock 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }



}
