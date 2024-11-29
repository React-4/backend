package org.pda.announcement.stockprice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockCurrentPriceResponse {
    private String ticker;
    private String name;
    private Double currentPrice;
    private Double changeRate;
    private Double changeAmount;
    private Double accTradeVolume;
    private Long marketCap;
    private Double foreignRatio;
}
