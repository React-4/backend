package org.pda.announcement.stockprice.service;

import java.util.List;

public interface StockPriceService {
    List<?> getStockPriceByTypeAndLength(Long stockId, String type, int length);
}
