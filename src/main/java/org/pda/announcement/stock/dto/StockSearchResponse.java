package org.pda.announcement.stock.dto;

import lombok.Data;
import org.pda.announcement.stock.domain.MarketType;
import org.pda.announcement.stock.domain.Stock;

@Data
public class StockSearchResponse {
    private Long stockId;
    private Long marketCap;
    private String ticker;
    private MarketType marketType;
    private String companyName;
    private String category;
    private String companyOverview;

    // Stock 엔티티를 StockSearchResponse로 변환하는 정적 메서드 추가
    public static StockSearchResponse fromStock(Stock stock) {
        StockSearchResponse response = new StockSearchResponse();
        response.setStockId(stock.getId());
        response.setMarketCap(stock.getMarketCap());
        response.setTicker(stock.getTicker());
        response.setMarketType(stock.getMarketType());
        response.setCompanyName(stock.getCompanyName());
        response.setCategory(stock.getCategory());
        response.setCompanyOverview(stock.getCompanyOverview());
        return response;
    }
}
