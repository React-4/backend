package org.pda.announcement.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pda.announcement.comment.domain.Comment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyCommentResponse {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private Long announcementId;
    private String announcementTitle;
    private Long stockId;
    private String stockName;

    public MyCommentResponse(Comment comment, String stockName, Long stockId, String announcementTitle, Long announcementId) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.announcementId = announcementId;
        this.announcementTitle = announcementTitle;
        this.stockId = stockId;
        this.stockName = stockName;
    }
}
