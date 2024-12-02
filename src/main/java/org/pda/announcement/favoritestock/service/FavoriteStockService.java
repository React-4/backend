package org.pda.announcement.favoritestock.service;

import org.pda.announcement.favoritestock.domain.FavoriteStock;
import org.pda.announcement.favoritestock.dto.FavoriteStockPriceResponse;
import org.pda.announcement.favoritestock.dto.FavoriteStockResponse;
import org.pda.announcement.stock.domain.Stock;
import org.pda.announcement.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteStockService {
    void addFavoriteStock(Long stockId, String authorization);

    void removeFavoriteStock(Long stockId, String email);

    List<FavoriteStockResponse> getFavoriteStockList(String email);

    boolean isFavoriteStock(Long stockId, String email);
<<<<<<< Updated upstream
    List<Long> getFavoriteStockIds(String userEmail);

=======

//    List<FavoriteStockPriceResponse> getStocksInfoByIds(List<Long> stockIds);
>>>>>>> Stashed changes
    }
