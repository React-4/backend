package org.pda.announcement.stockprice.repository;

import org.pda.announcement.stockprice.domain.StockPriceDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceDayRepository extends JpaRepository<StockPriceDay, Long>, StockPriceDayRepositoryCustom {

}
