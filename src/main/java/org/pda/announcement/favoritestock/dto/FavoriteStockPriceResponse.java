package org.pda.announcement.favoritestock.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteStockPriceResponse {
    private Long stockId;
    private String stockName;
    private String ticker;
    private Double currentPrice;
    private Double changeRate;
    private Double tradeVolume;
}

