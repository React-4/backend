package org.pda.announcement.stockprice.service;

import org.pda.announcement.stockprice.dto.StockCurrentPriceResponse;
import org.pda.announcement.stockprice.dto.StockRankResponse;

import java.util.List;
import java.util.Map;

public interface StockPriceService {
    Map<Integer, StockRankResponse> getStockRankFromRedis(String sortBy);  // Redis에서 순위 데이터를 가져오는 메서드
    List<?> getStockPriceByTypeAndLength(Long stockId, String type, int length);

    StockCurrentPriceResponse getStockCurrentPrice(String ticker);

    Map<String, StockCurrentPriceResponse> getStockCurrentPrices(List<String> tickers);

    Map<String, StockCurrentPriceResponse> getAllStockCurrentPrices();

    Map<Integer, StockRankResponse> getFavStockPriceFromRedis(List<Long> stockIds);

}
