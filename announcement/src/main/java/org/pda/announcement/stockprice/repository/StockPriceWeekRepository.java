package org.pda.announcement.stockprice.repository;

import org.pda.announcement.stockprice.domain.Stockpriceweek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceWeekRepository extends JpaRepository<Stockpriceweek, Long>, StockPriceWeekRepositoryCustom {

}
