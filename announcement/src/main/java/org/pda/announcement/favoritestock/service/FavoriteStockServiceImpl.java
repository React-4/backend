package org.pda.announcement.favoritestock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.favoritestock.repository.FavoriteStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteStockServiceImpl implements FavoriteStockService {

    private final FavoriteStockRepository favoriteStockRepository;

}
