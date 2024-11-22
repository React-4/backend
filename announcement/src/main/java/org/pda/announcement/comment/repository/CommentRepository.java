package org.pda.announcement.comment.repository;

import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.comment.domain.Comment;
import org.pda.announcement.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Page<Comment> findByUser(User user, Pageable pageable);

    Page<Comment> findByAnnouncement(Announcement announcement, Pageable pageable);

    List<Comment> findByAnnouncementIn(List<Announcement> announcements, Pageable pageable);
}
