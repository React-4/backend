package org.pda.announcement.announcement.domain;


import jakarta.persistence.*;
import lombok.*;
import org.pda.announcement.comment.domain.Comment;
import org.pda.announcement.feedback.domain.Feedback;
import org.pda.announcement.stock.domain.Stock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "Announcement")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announcement {
    @Id
    @Column(name = "announcement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "announcement_date", nullable = false)
    private LocalDate announcementDate;

    @Column(name = "submitter")
    private String submitter;

    @Column(name = "original_announcement_url")
    private String originalAnnouncementUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "announcement_type", nullable = false)
    private AnnouncementType announcementType;

    @Builder.Default
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void removeFeedBack(Feedback feedback) {
        this.feedbacks.remove(feedback);
    }
}

