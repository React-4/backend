package org.pda.announcement.stockprice.repository;


import org.pda.announcement.stockprice.domain.Stockpriceday;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockPriceDayRepository extends JpaRepository<Stockpriceday, Long>, StockPriceDayRepositoryCustom {


    List<Stockpriceday> findByStockIdOrderByDateDesc(Long stockId, Pageable pageable);

}
