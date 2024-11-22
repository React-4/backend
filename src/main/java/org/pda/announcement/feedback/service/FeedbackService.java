package org.pda.announcement.feedback.service;

import org.pda.announcement.feedback.domain.FeedbackType;
import org.pda.announcement.feedback.dto.FeedbackRequest;
import org.pda.announcement.feedback.dto.FeedbackResponse;
import org.pda.announcement.util.api.ApiCustomResponse;

public interface FeedbackService {
     FeedbackResponse getFeedback(Long announcementId);
     ApiCustomResponse addFeedback(Long announcementId, String token, FeedbackType feedbackRequest);
     void deleteFeedback(Long announcementId, String token);
    }
