package org.pda.announcement.stock.service;

import org.pda.announcement.stock.domain.Stock;
import org.pda.announcement.stock.dto.StockAutocompleteResponse;
import org.pda.announcement.stock.dto.StockSearchResponse;

import java.util.List;
import java.util.Optional;

public interface StockService {

    Optional<StockSearchResponse> getStockByTicker(String ticker);

    // 티커 및 회사명으로 자동완성 검색
    List<StockAutocompleteResponse> searchStocks(String keyword);
}
