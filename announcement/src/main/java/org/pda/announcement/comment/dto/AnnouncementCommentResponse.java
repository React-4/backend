package org.pda.announcement.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnnouncementCommentResponse {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private String userProfileColor;
}
