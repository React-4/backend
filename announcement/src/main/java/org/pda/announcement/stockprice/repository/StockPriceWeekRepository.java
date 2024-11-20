package org.pda.announcement.stockprice.repository;

import org.pda.announcement.stockprice.domain.StockPriceWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceWeekRepository extends JpaRepository<StockPriceWeek, Long>, StockPriceWeekRepositoryCustom {

}
