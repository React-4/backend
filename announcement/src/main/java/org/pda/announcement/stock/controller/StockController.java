package org.pda.announcement.stock.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.stock.service.StockService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;
}
