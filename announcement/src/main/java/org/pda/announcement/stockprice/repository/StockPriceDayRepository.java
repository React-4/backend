package org.pda.announcement.stockprice.repository;

import org.pda.announcement.stockprice.domain.Stockpriceday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceDayRepository extends JpaRepository<Stockpriceday, Long>, StockPriceDayRepositoryCustom {

}
