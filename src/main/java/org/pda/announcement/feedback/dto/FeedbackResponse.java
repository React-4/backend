package org.pda.announcement.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedbackResponse {
    private int totalVoteCount;
    private int positiveVoteCount;
    private int negativeVoteCount;
}
