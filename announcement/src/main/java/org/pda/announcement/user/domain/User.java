package org.pda.announcement.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.pda.announcement.comment.domain.Comment;
import org.pda.announcement.favoriteannouncement.domain.FavoriteAnnouncement;
import org.pda.announcement.favoritestock.domain.FavoriteStock;
import org.pda.announcement.feedback.domain.Feedback;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "User")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birth_date", length = 7)
    private String birthDate;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "profile_color", length = 20)
    private String profileColor;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType = UserType.GUEST;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteStock> favoriteStocks = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteAnnouncement> favoriteAnnouncements = new ArrayList<>();

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void removeFeedback(Feedback feedback) {
        this.feedbacks.remove(feedback);
    }

    public void removeFavoriteAnnouncement(FavoriteAnnouncement favoriteAnnouncement) {
        this.favoriteAnnouncements.remove(favoriteAnnouncement);
    }

    public void removeFavoriteStock(FavoriteStock favoriteStock) {
        this.favoriteStocks.remove(favoriteStock);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String encode) {
        this.passwordHash = encode;
    }
}