package org.pda.announcement.stockprice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.stockprice.repository.StockPriceDayRepository;
import org.pda.announcement.stockprice.repository.StockPriceMonthRepository;
import org.pda.announcement.stockprice.repository.StockPriceWeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockPriceServiceImpl implements StockPriceService {

    private final StockPriceDayRepository stockPriceDayRepository;
    private final StockPriceMonthRepository stockPriceMonthRepository;
    private final StockPriceWeekRepository stockPriceWeekRepository;

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
}