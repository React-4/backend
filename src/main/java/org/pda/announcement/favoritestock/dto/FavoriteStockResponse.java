package org.pda.announcement.favoritestock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteStockResponse {
    private Long stockId;
    private String companyName;
    private Double currentPrice;
    private Double changeRate;
}
