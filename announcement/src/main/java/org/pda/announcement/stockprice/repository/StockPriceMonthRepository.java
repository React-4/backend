package org.pda.announcement.stockprice.repository;

import org.pda.announcement.stockprice.domain.StockPriceMonth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceMonthRepository extends JpaRepository<StockPriceMonth, Long>, StockPriceMonthRepositoryCustom {

}
