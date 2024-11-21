package org.pda.announcement.favoriteannouncement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.user.domain.User;


@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "favorite_announcement")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteAnnouncement {
    @Id
    @Column(name = "favorite_announcement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
    private Announcement announcement;
}
