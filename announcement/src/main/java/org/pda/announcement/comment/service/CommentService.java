package org.pda.announcement.comment.service;

import org.pda.announcement.comment.dto.CommentRequest;

public interface CommentService {
    void createComment(Long announcementId, String content, String email);

    void updateComment(Long commentId, CommentRequest commentRequest, String email);
}
