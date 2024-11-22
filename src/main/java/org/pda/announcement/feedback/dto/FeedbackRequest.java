package org.pda.announcement.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedbackRequest {
    private String type; // "positive" or "negative"
}
