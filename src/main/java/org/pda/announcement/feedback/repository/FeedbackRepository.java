package org.pda.announcement.feedback.repository;

import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.feedback.domain.Feedback;
import org.pda.announcement.feedback.domain.FeedbackType;
import org.pda.announcement.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {
    // 특정 공시에 대한 총 투표 수
    int countByAnnouncement(Announcement announcement);

    // 특정 공시에 대한 특정 타입(긍정/부정) 투표 수
    int countByAnnouncementAndType(Announcement announcement, FeedbackType type);

    // 사용자와 공시 ID로 피드백 찾기
    Optional<Feedback> findByUserAndAnnouncement(User user, Announcement announcement);

    // 특정 공시에 대한 모든 피드백 조회
    List<Feedback> findByAnnouncement(Announcement announcement);

    // 사용자와 공시의 피드백 존재 여부 확인
    boolean existsByUserAndAnnouncement(User user, Announcement announcement);
}
