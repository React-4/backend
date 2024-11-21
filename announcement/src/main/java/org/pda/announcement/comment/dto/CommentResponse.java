package org.pda.announcement.comment.dto;

import lombok.Data;

@Data
public class CommentResponse {
    private int comment_id;
    private String content;
    private String created_at;
    private String user_id;
    private int stock_id;
}
