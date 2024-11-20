package org.pda.announcement.favoritestock.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.favoritestock.service.FavoriteStockService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite/stock")
public class FavoriteStockController {

    private final FavoriteStockService favoriteStockService;
}
