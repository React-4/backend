package org.pda.announcement.favoritestock.repository;

import org.pda.announcement.favoritestock.domain.FavoriteStock;
import org.pda.announcement.favoritestock.dto.FavoriteStockPriceResponse;
import org.pda.announcement.stock.domain.Stock;
import org.pda.announcement.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteStockRepository extends JpaRepository<FavoriteStock, Long>, FavoriteStockRepositoryCustom {

    boolean existsByUserAndStock(User user, Stock stock);

    Optional<FavoriteStock> findByUserAndStock(User user, Stock stock);

    List<FavoriteStock> findByUser(User user);

    @Query("SELECT fs.stock.id FROM FavoriteStock fs WHERE fs.user.email = :email")
    List<Long> findStockIdsByUserEmail(@Param("email") String userEmail);


}
