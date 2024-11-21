package org.pda.announcement.stockprice.repository;

import org.pda.announcement.stockprice.domain.Stockpricemonth;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockPriceMonthRepository extends JpaRepository<Stockpricemonth, Long>, StockPriceMonthRepositoryCustom {



    List<Stockpricemonth> findByStockIdOrderByDateDesc(Long stockId, Pageable pageable);

}
