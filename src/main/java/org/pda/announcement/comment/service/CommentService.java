package org.pda.announcement.comment.service;

import org.pda.announcement.comment.dto.AnnouncementCommentResponse;
import org.pda.announcement.comment.dto.CommentRequest;
import org.pda.announcement.comment.dto.MyCommentResponse;
import org.pda.announcement.comment.dto.StockCommentResponse;

import java.util.List;

public interface CommentService {
    Long createComment(Long announcementId, String content, String email);

    void updateComment(Long commentId, CommentRequest commentRequest, String email);

    void deleteComment(Long commentId, String email);

    List<MyCommentResponse> getMyComments(String email, int page, int size);

    List<AnnouncementCommentResponse> getCommentsByAnnouncement(Long announcementId, int page, int size);

    List<StockCommentResponse> getLatestCommentsByStock(Long stockId, int page, int size);
}
