package org.pda.announcement.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StockCommentResponse {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private String userProfileColor;
    private Long announcementId;
    private String announcementTitle;
}
