package org.pda.announcement.stock.dto;

import org.pda.announcement.stock.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAutocompleteResponse {

    private Long id; // id -> int
    private String ticker; // ticker -> string
    private String companyName; // company_name -> string

    // Stock 엔티티를 StockAutocompleteDTO로 변환하는 정적 메서드 추가
    public static StockAutocompleteResponse fromStock(Stock stock) {
        return new StockAutocompleteResponse(
                stock.getId(),
                stock.getTicker(),
                stock.getCompanyName()
        );
    }
}
