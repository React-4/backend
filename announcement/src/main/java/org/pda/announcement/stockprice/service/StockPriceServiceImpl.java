package org.pda.announcement.stockprice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.stockprice.repository.StockPriceDayRepository;
import org.pda.announcement.stockprice.repository.StockPriceMonthRepository;
import org.pda.announcement.stockprice.repository.StockPriceWeekRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockPriceServiceImpl implements StockPriceService {

    private final StockPriceDayRepository stockPriceDayRepository;
    private final StockPriceMonthRepository stockPriceMonthRepository;
    private final StockPriceWeekRepository stockPriceWeekRepository;

}
