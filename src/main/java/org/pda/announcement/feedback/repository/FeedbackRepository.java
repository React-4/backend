package org.pda.announcement.feedback.repository;

import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.feedback.domain.Feedback;
import org.pda.announcement.feedback.domain.FeedbackType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {

    int countByAnnouncementAndType(Announcement announcement, FeedbackType type);
}
