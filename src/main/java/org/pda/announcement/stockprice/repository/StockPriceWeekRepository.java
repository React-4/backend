package org.pda.announcement.stockprice.repository;

import org.pda.announcement.stockprice.domain.Stockpriceweek;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StockPriceWeekRepository extends JpaRepository<Stockpriceweek, Long>, StockPriceWeekRepositoryCustom {

    List<Stockpriceweek> findByStockIdOrderByDateDesc(Long stockId, Pageable pageable);

}
