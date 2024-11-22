package org.pda.announcement.stock.repository;

import org.pda.announcement.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryCustom {

    // Ticker로 주식 찾기
    Optional<Stock> findByTicker(String ticker);
    List<Stock> findByTickerContainingOrCompanyNameContaining(String ticker, String companyName);

}
