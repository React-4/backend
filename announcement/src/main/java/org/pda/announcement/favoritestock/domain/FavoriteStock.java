package org.pda.announcement.favoritestock.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.pda.announcement.stock.domain.Stock;
import org.pda.announcement.user.domain.User;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "favorite_stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteStock {
    @Id
    @Column(name = "favorite_stock_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Stock 삭제 시 FavoriteStock도 삭제
    private Stock stock;


}

