package org.pda.announcement.favoritestock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.exception.GlobalCustomException;
import org.pda.announcement.favoritestock.domain.FavoriteStock;
import org.pda.announcement.favoritestock.dto.FavoriteStockPriceResponse;
import org.pda.announcement.favoritestock.dto.FavoriteStockResponse;
import org.pda.announcement.favoritestock.repository.FavoriteStockRepository;
import org.pda.announcement.stock.domain.Stock;
import org.pda.announcement.stock.repository.StockRepository;
import org.pda.announcement.stockprice.service.RedisService;
import org.pda.announcement.user.domain.User;
import org.pda.announcement.user.repository.UserRepository;
import org.pda.announcement.util.security.jwt.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteStockServiceImpl implements FavoriteStockService {

    private final FavoriteStockRepository favoriteStockRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisService redService;
    // 관심 종목 추가

    @Override
    public void addFavoriteStock(Long stockId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(GlobalCustomException.UserNotFoundException::new);

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 stock_id"));

        // 이미 존재하는지 확인
        if (favoriteStockRepository.existsByUserAndStock(user, stock)) {
            throw new IllegalArgumentException("이미 관심 종목에 추가됨");
        }

        FavoriteStock favoriteStock = FavoriteStock.builder().user(user).stock(stock).build();
        favoriteStockRepository.save(favoriteStock);
    }

    // 관심 종목 삭제
    @Override
    public void removeFavoriteStock(Long stockId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(GlobalCustomException.UserNotFoundException::new);

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 stock_id"));

        // 관심 종목이 눌리지 않은 경우
        FavoriteStock favoriteStock = favoriteStockRepository.findByUserAndStock(user, stock)
                .orElseThrow(() -> new IllegalArgumentException("관심 종목이 눌리지 않았습니다"));

        favoriteStockRepository.delete(favoriteStock);
    }

    @Override
    public List<FavoriteStockResponse> getFavoriteStockList(String email) {
        // 이메일로 사용자 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

        // 사용자의 관심 종목 목록 가져오기
        List<FavoriteStock> favoriteStocks = favoriteStockRepository.findByUser(user);

        // 관심 종목에서 필요한 정보만 추출하여 DTO로 반환
        return favoriteStocks.stream()
                .map(favoriteStock -> {
                    Stock stock = favoriteStock.getStock();
                    Map<Object, Object> stockPrice = redService.getStockCurrentPriceByTicker(stock.getTicker());
                    double currentPrice = Double.parseDouble((String) stockPrice.get("currentPrice"));
                    double changeRate = Double.parseDouble((String) stockPrice.get("changeRate"));

                    return new FavoriteStockResponse(
                            stock.getId(),
                            stock.getCompanyName(),
                            changeRate,
                            changeRate
                    );
                })
                .collect(Collectors.toList());
    }

    // 관심 종목에 이미 추가되었는지 확인
    @Override
    public boolean isFavoriteStock(Long stockId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(GlobalCustomException.UserNotFoundException::new);

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 stock_id"));

        return favoriteStockRepository.existsByUserAndStock(user, stock);
    }

<<<<<<< Updated upstream
    public List<Long> getFavoriteStockIds(String userEmail) {
        // 유저의 관심 종목 ID 리스트 반환
        return favoriteStockRepository.findStockIdsByUserEmail(userEmail);
    }

=======
//    @Override
//    public List<FavoriteStockPriceResponse> getStocksInfoByIds(List<Long> stockIds) {
//        return favoriteStockRepository.findStocksInfoByIds(stockIds).stream()
//                .map(stock -> new FavoriteStockPriceResponse(
//                        stock.getStockId(),
//                        stock.getStockName(),
//                        stock.getTicker(),
//                        stock.getCurrentPrice(),
//                        stock.getChangeRate(),
//                        stock.getTradeVolume()
//                ))
//                .collect(Collectors.toList());
//    }
>>>>>>> Stashed changes
}
