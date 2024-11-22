package org.pda.announcement.announcement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pda.announcement.announcement.domain.Announcement;
import org.pda.announcement.announcement.dto.AllAnnouncementResponse;
import org.pda.announcement.announcement.dto.AllAnnouncementsResponse;
import org.pda.announcement.announcement.dto.AnnouncementResponse;
import org.pda.announcement.announcement.repository.AnnouncementRepository;
import org.pda.announcement.comment.repository.CommentRepository;
import org.pda.announcement.exception.GlobalCustomException.AnnouncementNotFoundException;
import org.pda.announcement.feedback.domain.FeedbackType;
import org.pda.announcement.feedback.repository.FeedbackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final CommentRepository commentRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public AnnouncementResponse getAnnouncementById(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new AnnouncementNotFoundException("유효하지 않은 announcement_id"));

        int commentCount = commentRepository.countByAnnouncement(announcement);
        int positiveVoteCount = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.POSITIVE);
        int negativeVoteCount = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.NEGATIVE);

        return new AnnouncementResponse(
                announcement.getStock().getId(),
                announcement.getTitle(),
                announcement.getContent(),
                announcement.getAnnouncementDate(),
                announcement.getSubmitter(),
                announcement.getAnnouncementType(),
                announcement.getOriginalAnnouncementUrl(),
                positiveVoteCount + negativeVoteCount,
                positiveVoteCount,
                negativeVoteCount,
                commentCount
        );
    }

    @Override
    public AllAnnouncementsResponse getAllAnnouncements(String sortBy, Pageable pageable) {
        Sort sort = switch (sortBy) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "announcementDate");
            case "most_comments" -> Sort.by(Sort.Direction.DESC, "commentCount");
            case "most_votes" -> Sort.by(Sort.Direction.DESC, "totalFeedbackCount");
            default -> throw new IllegalArgumentException();
        };

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Announcement> announcements = announcementRepository.findAll(sortedPageable);

        List<AllAnnouncementResponse> announcementList = announcements.stream().map(announcement -> {
            int commentCount = commentRepository.countByAnnouncement(announcement);
            int positiveVoteCount = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.POSITIVE);
            int negativeVoteCount = feedbackRepository.countByAnnouncementAndType(announcement, FeedbackType.NEGATIVE);

            return new AllAnnouncementResponse(
                    announcement.getAnnouncementId(),
                    announcement.getTitle(),
                    announcement.getStock().getCompanyName(),
                    announcement.getAnnouncementDate(),
                    announcement.getSubmitter(),
                    positiveVoteCount + negativeVoteCount,
                    positiveVoteCount,
                    negativeVoteCount,
                    commentCount
            );
        }).collect(Collectors.toList());

        return new AllAnnouncementsResponse(announcements.getTotalPages(), announcementList);
    }

}
