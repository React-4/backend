package org.pda.announcement.feedback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.feedback.domain.Feedback;
import org.pda.announcement.feedback.domain.FeedbackType;
import org.pda.announcement.feedback.dto.FeedbackRequest;
import org.pda.announcement.feedback.dto.FeedbackResponse;
import org.pda.announcement.feedback.repository.FeedbackRepository;
import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.announcement.repository.AnnouncementRepository;
import org.pda.announcement.user.domain.User;
import org.pda.announcement.user.repository.UserRepository;
import org.pda.announcement.util.api.ApiCustomResponse;
import org.pda.announcement.util.security.jwt.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    // 특정 공시 투표 조회
    @Override
    public FeedbackResponse getFeedback(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 announcement_id"));

        int totalVotes = feedbackRepository.countByAnnouncement(announcement);
        int positiveVotes = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.POSITIVE);
        int negativeVotes = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.NEGATIVE);

        return new FeedbackResponse(totalVotes, positiveVotes, negativeVotes);
    }

    // 특정 공시 투표 추가
    @Override
    public ApiCustomResponse addFeedback(Long announcementId, String token, FeedbackType feedbackRequest) {
        String userEmail = jwtService.getUserEmailByJWT(token);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 announcement_id"));

        // 기존에 해당 공시에 대한 투표가 이미 있는지 확인
        Feedback existingFeedback = feedbackRepository.findByUserAndAnnouncement(user, announcement).orElse(null);

        if (existingFeedback != null) {
            // 기존 투표가 있고, 요청한 투표와 다르면 기존 투표를 삭제하고 새 투표 추가
            if (existingFeedback.getType() != feedbackRequest) {
                feedbackRepository.delete(existingFeedback); // 기존 투표 삭제
            } else {
                throw new IllegalArgumentException("이미 투표한 상태입니다"); // 동일한 투표가 이미 존재하는 경우
            }
        }

        // 새 투표 추가
        Feedback feedback = Feedback.builder().announcement(announcement).user(user).type(feedbackRequest).build();
        feedbackRepository.save(feedback);

        // 투표 결과 반환
        return new ApiCustomResponse("투표 추가 성공", List.of(
                feedbackRepository.countByAnnouncement(announcement),
                feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.POSITIVE),
                feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.NEGATIVE)
        ));
    }

    // 특정 공시 투표 삭제
    @Override
    public void deleteFeedback(Long announcementId, String token) {
        String userEmail = jwtService.getUserEmailByJWT(token);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 announcement_id"));

        Feedback feedback = feedbackRepository.findByUserAndAnnouncement(user, announcement)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 투표가 없습니다"));

        feedbackRepository.delete(feedback);
    }
}
