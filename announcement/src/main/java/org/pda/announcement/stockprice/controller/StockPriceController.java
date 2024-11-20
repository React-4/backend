package org.pda.announcement.stockprice.controller;

import lombok.RequiredArgsConstructor;
import org.pda.announcement.stockprice.service.StockPriceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stockprice")
public class StockPriceController {

    private final StockPriceService stockPriceService;
}
