package org.pda.announcement.stock.domain;

import jakarta.persistence.*;
import lombok.*;
import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.stockprice.domain.Stockpriceday;
import org.pda.announcement.stockprice.domain.Stockpricemonth;
import org.pda.announcement.stockprice.domain.Stockpriceweek;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "Stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {
    @Id
    @Column(name = "stock_id", length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "market_cap")
    private Long marketCap;

    @Column(name = "ticker", nullable = false)
    private String ticker;

    @Enumerated(EnumType.STRING)
    @Column(name = "market_type", nullable = false)
    private MarketType marketType;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "category")
    private String category;

    @Column(name = "company_overview")
    private String companyOverview;

//    @Builder.Default
//    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<StockPriceDay> stockPriceDays = new ArrayList<>();
//
//    @Builder.Default
//    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<StockPriceWeek> stockPriceWeeks = new ArrayList<>();
//
//    @Builder.Default
//    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<StockPriceMonth> stockPriceMonths = new ArrayList<>();


    @Builder.Default
    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Announcement> announcements = new ArrayList<>();
}
