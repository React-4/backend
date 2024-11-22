package org.pda.announcement.stockprice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockRankResponse {
    @JsonProperty("종목코드")  // JSON에서 "종목코드"를 자바 필드 "stockCode"로 매핑
    private String stockCode;

    @JsonProperty("현재가")  // JSON에서 "현재가"를 자바 필드 "currentPrice"로 매핑
    private String currentPrice;

    @JsonProperty("등락률")  // JSON에서 "등락률"을 자바 필드 "changeRate"로 매핑
    private String changeRate;

    @JsonProperty("거래량")  // JSON에서 "거래량"을 자바 필드 "volume"으로 매핑
    private String volume;
}