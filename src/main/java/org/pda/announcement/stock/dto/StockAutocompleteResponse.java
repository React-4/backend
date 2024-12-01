package org.pda.announcement.stock.dto;

import org.pda.announcement.stock.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAutocompleteResponse {

    private Long id;              // 주식 ID
    private String ticker;        // 티커
    private String companyName;   // 회사명
    private Double currentPrice;  // 현재가
    private Double changeRate;    // 등락률
    private Long volume;          // 거래량

    // Stock 엔티티를 StockAutocompleteDTO로 변환하는 정적 메서드 추가
    public static StockAutocompleteResponse fromStock(Stock stock, Double currentPrice, Double changeRate, Long volume) {
        return new StockAutocompleteResponse(
                stock.getId(),
                stock.getTicker(),
                stock.getCompanyName(),
                currentPrice,
                changeRate,
                volume
        );
    }
}
