package org.pda.announcement.stockprice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.pda.announcement.stock.domain.Stock;

import java.time.LocalDate;

@Entity
@Table(name = "stockpricemonth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stockpricemonth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_price_month_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "open_price", nullable = false)
    private Integer openPrice;

    @NotNull
    @Column(name = "high_price", nullable = false)
    private Integer highPrice;

    @NotNull
    @Column(name = "low_price", nullable = false)
    private Integer lowPrice;

    @NotNull
    @Column(name = "close_price", nullable = false)
    private Integer closePrice;

    @Column(name = "volume")
    private Integer volume;

    @Column(name = "change_rate")
    private Float changeRate;

}

