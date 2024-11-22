package org.pda.announcement.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.stock.domain.Stock;
import org.pda.announcement.stock.dto.StockAutocompleteResponse;
import org.pda.announcement.stock.dto.StockSearchResponse;
import org.pda.announcement.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public Optional<StockSearchResponse> getStockByTicker(String ticker) {
        Optional<Stock> stock = stockRepository.findByTicker(ticker);
        return stock.map(StockSearchResponse::fromStock); // Stock 엔티티를 DTO로 변환하여 반환
    }

    @Override
    public List<StockAutocompleteResponse> searchStocks(String keyword) {

        List<Stock> stocks = stockRepository.findByTickerContainingOrCompanyNameContaining(keyword, keyword);
        return stocks.stream()
                .map(StockAutocompleteResponse::fromStock) // Stock 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }


}
