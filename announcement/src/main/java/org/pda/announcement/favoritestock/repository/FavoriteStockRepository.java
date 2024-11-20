package org.pda.announcement.favoritestock.repository;

import org.pda.announcement.favoritestock.domain.FavoriteStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStockRepository extends JpaRepository<FavoriteStock, Long>, FavoriteStockRepositoryCustom {

}
