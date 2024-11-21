package org.pda.announcement.stockprice.repository;

import org.pda.announcement.stockprice.domain.Stockpricemonth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceMonthRepository extends JpaRepository<Stockpricemonth, Long>, StockPriceMonthRepositoryCustom {

}
