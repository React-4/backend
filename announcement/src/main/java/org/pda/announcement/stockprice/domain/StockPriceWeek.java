package org.pda.announcement.stockprice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.pda.announcement.stock.domain.Stock;

import java.time.LocalDate;

@Entity
@Table(name = "StockPriceWeek")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockPriceWeek {
    @Id
    @Column(name = "stock_price_week_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "open_price", nullable = false)
    private Integer openPrice;

    @Column(name = "high_price", nullable = false)
    private Integer highPrice;

    @Column(name = "low_price", nullable = false)
    private Integer lowPrice;

    @Column(name = "close_price", nullable = false)
    private Integer closePrice;

    @Column(name = "volume")
    private Integer volume;

    @Column(name = "change_rate")
    private Float changeRate;
}
