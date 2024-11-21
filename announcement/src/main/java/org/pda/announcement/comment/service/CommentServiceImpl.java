package org.pda.announcement.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.announcement.repository.AnnouncementRepository;
import org.pda.announcement.comment.domain.Comment;
import org.pda.announcement.comment.dto.CommentRequest;
import org.pda.announcement.comment.repository.CommentRepository;
import org.pda.announcement.exception.GlobalCustomException;
import org.pda.announcement.exception.GlobalCustomException.AnnouncementNotFoundException;
import org.pda.announcement.exception.GlobalCustomException.UserNotFoundException;
import org.pda.announcement.user.domain.User;
import org.pda.announcement.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public void createComment(Long announcementId, String content, String email) {
        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        // 공시 조회
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(AnnouncementNotFoundException::new);

        // 댓글 객체 생성
        Comment comment = Comment.builder()
                .content(content)
                .announcement(announcement)
                .user(user)
                .build();

        // 댓글 저장
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, CommentRequest commentRequest, String email) {

        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(GlobalCustomException.CommentNotFoundException::new);

        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        // 댓글 작성자와 요청자가 다를 경우 예외 처리
        if (!comment.getUser().equals(user)) {
            throw new GlobalCustomException.UnauthorizedException("댓글 작성자만 수정할 수 있습니다.");
        }

        // 댓글 수정
        comment.updateContent(commentRequest.getContent());
        commentRepository.save(comment);
    }
}
